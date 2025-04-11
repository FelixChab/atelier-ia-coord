<template>
  <div class="history-section">
    <h2>Historique des analyses</h2>
    <button v-if="!loading && !history.length == 0" @click="deleteAllItem()" class="delete-btn">Supprimer tout l'historique</button>

    <div v-if="loading && !displayedHistory.length" class="loading">
      <p>Chargement de l'historique...</p>
    </div>

    <div v-else-if="!history || history.length === 0" class="empty-history">
      <p>Aucun historique d'analyse trouvé.</p>
    </div>

    <div v-else class="history-list">
      <div v-for="item in displayedHistory" :key="item.id" class="history-item">
        <div class="history-header">
          <span class="history-type">Analyse de qualité</span>
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
          </div>
        </div>

        <div v-if="item.correctionDetails && item.correctionDetails.length > 0" class="correction-details">
          <h4>Détails des corrections:</h4>
          <ul>
            <li v-for="(correction, index) in item.correctionDetails" :key="index" class="correction-content">
              <span class="original">{{ correction.original }} →</span>
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

      <div v-if="hasMoreItems" class="see-more-container">
        <button
            @click="loadMoreItems"
            class="see-more-btn"
            :disabled="loadingMore"
        >
          {{ loadingMore ? 'Chargement...' : 'Voir plus' }}
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, watch, onMounted } from 'vue'
import axios from 'axios'
import "@/assets/styles/analysisHistory.css"

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
    const loadingMore = ref(false)
    const error = ref(null)
    const itemsPerPage = 5
    const currentPage = ref(1)

    // Computed property to get only the items to display
    const displayedHistory = computed(() => {
      return history.value.slice(0, currentPage.value * itemsPerPage)
    })

    // Computed property to check if there are more items to load
    const hasMoreItems = computed(() => {
      return displayedHistory.value.length < history.value.length
    })

    async function fetchHistory() {
      loading.value = true

      try {
        // Fetch all history items at once
        const response = await axios.get('http://localhost:8081/api/corrections')
        history.value = response.data
        // Reset to first page when refreshing
        currentPage.value = 1
      } catch (err) {
        console.error('Error fetching history:', err)
        error.value = 'Impossible de charger l\'historique.'
      } finally {
        loading.value = false
      }
    }

    // Function to load more items
    function loadMoreItems() {
      loadingMore.value = true

      // Simulate a delay to show loading state
      setTimeout(() => {
        currentPage.value++
        loadingMore.value = false
      }, 500)
    }

    async function deleteItem(id) {
      try {
        await axios.delete(`http://localhost:8081/api/corrections/history/${id}`)
        // Remove the deleted item from the local history array
        history.value = history.value.filter(item => item.id !== id)
        // Emit an event to notify parent component
        emit('item-deleted', id)
      } catch (err) {
        console.error('Error deleting history item:', err)
        error.value = 'Impossible de supprimer cet élément.'
      }
    }

    async function deleteAllItem() {
      try {
        await axios.delete(`http://localhost:8081/api/corrections/history`)
        // Remove the deleted item from the local history array
        history.value = []
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
      displayedHistory,
      hasMoreItems,
      loading,
      loadingMore,
      error,
      fetchHistory,
      loadMoreItems,
      deleteItem,
      deleteAllItem,
      formatDate,
      formatCorrectedText
    }
  }
}
</script>
