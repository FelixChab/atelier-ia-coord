<template>
  <div class="logs-section">
    <h2>Logs des interactions IA</h2>

    <div v-if="loading" class="loading">
      <p>Chargement des logs...</p>
    </div>

    <div v-else-if="!logs || logs.length === 0" class="empty-logs">
      <p>Aucun log trouvé.</p>
    </div>

    <div v-else class="logs-list">
      <div v-for="log in logs" :key="log.id" class="log-item">
        <div class="log-header">
          <span class="log-date">{{ formatDate(log.timestamp) }}</span>
          <span class="log-status" :class="{ success: log.status, failed: !log.status }">
            {{ log.status ? 'Succès' : 'Échec' }}
          </span>
        </div>

        <div class="log-body">
          <p><strong>Texte envoyé :</strong> {{ log.requestText }}</p>
          <p><strong>Réponse IA :</strong> {{ log.responseText }}</p>
          <p v-if="!log.status && log.errorMessage" class="error-message">
            <strong>Erreur :</strong> {{ log.errorMessage }}
          </p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const logs = ref([])
const loading = ref(false)
const error = ref(null)

async function fetchLogs() {
  loading.value = true
  try {
    const response = await axios.get('http://localhost:8081/api/logs') // à adapter selon ton endpoint
    logs.value = response.data
  } catch (err) {
    console.error('Erreur lors du chargement des logs :', err)
    error.value = 'Impossible de charger les logs.'
  } finally {
    loading.value = false
  }
}

function formatDate(dateString) {
  const date = new Date(dateString)
  return date.toLocaleString('fr-FR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    hour: '2-digit',
    minute: '2-digit',
  })
}

onMounted(() => {
  fetchLogs()
})
</script>
<style scoped>
.logs-section {
  margin-top: 40px;
  padding: 20px;
  background-color: #f3f3f3;
  border-radius: 8px;
}

.logs-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.log-item {
  background: white;
  padding: 15px;
  border-radius: 6px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

.log-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 10px;
}

.log-status.success {
  color: #4caf50;
  font-weight: bold;
}

.log-status.failed {
  color: #f44336;
  font-weight: bold;
}

.error-message {
  color: #d32f2f;
  margin-top: 10px;
}
</style>