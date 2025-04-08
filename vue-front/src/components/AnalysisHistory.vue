<!-- src/components/AnalysisHistory.vue -->
<template>
  <div class="history-section">
    <h2>Historique des analyses</h2>

    <div v-if="loading" class="loading">
      <p>Chargement de l'historique...</p>
    </div>

    <div v-else-if="!history || history.length === 0" class="empty-history">
      <p>Aucun historique d'analyse trouvé.</p>
    </div>

    <div v-else class="history-list">
      <div v-for="item in history" :key="item.id" class="history-item">
        <div class="history-header">
          <span class="history-type">Analyse de qualité du français</span>
          <span class="history-date">{{ formatDate(item.createdAt) }}</span>
        </div>

        <div class="history-content">
          <div class="history-text-container">
            <div class="original-text">
              <h4>Texte original:</h4>
              <p>{{ item.originalText }}</p>
            </div>

            <div class="corrected-text">
              <h4>Texte corrigé:</h4>
              <div v-html="formatCorrectedText(item.correctedText)"></div>
            </div>
          </div>

          <div class="history-stats">
            <p class="quality-score">
              Score de qualité: <strong>{{ item.qualityScore }}/10</strong>
            </p>
            <p class="error-count">
              Erreurs: <strong>{{ item.errorCount }}</strong>
            </p>
            <p class="status" :class="item.status.toLowerCase()">
              Statut: <strong>{{ item.status }}</strong>
            </p>
          </div>
        </div>

        <div v-if="item.correctionDetails && item.correctionDetails.length > 0" class="correction-details">
          <h4>Détails des corrections:</h4>
          <ul>
            <li v-for="(correction, index) in item.correctionDetails" :key="index">
              <span class="original">{{ correction.original }}</span> →
              <span class="corrected">{{ correction.corrected }}</span>
              <span v-if="correction.explanation" class="explanation">
                ({{ correction.explanation }})
              </span>
            </li>
          </ul>
        </div>

        <div class="history-actions">
          <button @click="deleteItem(item.id)" class="delete-btn">Supprimer</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, watch, onMounted } from 'vue'
import axios from 'axios'

export default {
  name: 'AnalysisHistory',
  props: {
    refreshTrigger: {
      type: Number,
      default: 0
    }
  },
  emits: ['item-deleted'],
  setup(props, { emit }) {
    const history = ref([])
    const loading = ref(false)
    const error = ref(null)

    async function fetchHistory() {
      loading.value = true

      try {
        const response = await axios.get('http://localhost:8081/api/corrections')
        history.value = response.data
      } catch (err) {
        console.error('Error fetching history:', err)
        error.value = 'Impossible de charger l\'historique.'
      } finally {
        loading.value = false
      }
    }

    async function deleteItem(id) {
      try {
        await axios.delete(`/api/history/${id}`)
        // Remove the deleted item from the local history array
        history.value = history.value.filter(item => item.id !== id)
        // Emit an event to notify parent component
        emit('item-deleted', id)
      } catch (err) {
        console.error('Error deleting history item:', err)
        error.value = 'Impossible de supprimer cet élément.'
      }
    }

    function formatDate(timestamp) {
      if (!timestamp) return 'Date inconnue'
      const date = new Date(timestamp)
      return date.toLocaleString('fr-FR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit'
      })
    }

    function formatCorrectedText(text) {
      if (!text) return ''
      // The corrected text contains markdown-style formatting, so we'll render it as HTML
      return text.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    }

    // Watch for changes to refreshTrigger to reload history
    watch(() => props.refreshTrigger, () => {
      fetchHistory()
    })

    // Fetch history when component is mounted
    onMounted(() => {
      fetchHistory()
    })

    return {
      history,
      loading,
      error,
      fetchHistory,
      deleteItem,
      formatDate,
      formatCorrectedText
    }
  }
}
</script>

<style scoped>
.analysis-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.analysis-options {
  display: flex;
  margin-bottom: 20px;
}

.analysis-options button {
  padding: 10px 15px;
  margin-right: 10px;
  background-color: #f0f0f0;
  border: 1px solid #ddd;
  border-radius: 4px;
  cursor: pointer;
}

.analysis-options button.active {
  background-color: #4caf50;
  color: white;
}

.input-section {
  margin-bottom: 20px;
}

textarea {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 16px;
  margin-bottom: 10px;
}

.analyze-btn {
  padding: 12px 20px;
  background-color: #2196f3;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.analyze-btn:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.result-section {
  background-color: #f9f9f9;
  padding: 20px;
  border-radius: 4px;
}

.result-card {
  background-color: white;
  padding: 15px;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.confidence,
.feedback {
  margin-top: 10px;
}

.error-message {
  color: #f44336;
  margin-top: 20px;
  padding: 10px;
  background-color: #ffebee;
  border-radius: 4px;
}
</style>
