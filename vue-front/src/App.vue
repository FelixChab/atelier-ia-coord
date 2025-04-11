<script>
import axios from "axios";
import SpellcheckerForm from "./components/SpellcheckerForm.vue";
import AnalysisHistory from "./components/AnalysisHistory.vue";
import '@/assets/styles/app.css'

export default {
  name: "App",
  components: {
    SpellcheckerForm,
    AnalysisHistory,
  },
  data() {
    return {
      loading: false,
      error: null,
      historyRefreshTrigger: 0,
    };
  },
  methods: {
    async handleCheckText(payload) {
      this.loading = true;

      try {
        // Make API call to the French analysis endpoint
        const response = await axios.post(
            "http://localhost:8081/api/corrections",
            payload
        );

        // Update the result in the SpellcheckerForm component
        this.$refs.spellcheckerForm.updateResult(response.data);

        // Trigger history refresh after successful analysis
        this.refreshHistory();
      } catch (error) {
        console.error("Analysis error:", error);
        const errorMessage =
            error.response?.data?.message ||
            "Une erreur est survenue lors de l'analyse. Veuillez réessayer.";

        // Set error in the SpellcheckerForm component
        this.$refs.spellcheckerForm.setError(errorMessage);
      } finally {
        this.loading = false;
      }
    },

    refreshHistory() {
      // Increment the trigger to cause the history component to refresh
      this.historyRefreshTrigger += 1;
    },

    handleHistoryItemDeleted(id) {
      console.log(`History item deleted: ${id}`);
      // You can add additional logic here if needed when an item is deleted
    },
  },
};
</script>

<template>
  <div id="app">
    <header>
      <h1>CorrectIA</h1>
      <p class="tagline">Le correcteur français le moins précis au monde</p>
    </header>

    <main>
      <spellchecker-form ref="spellcheckerForm" @check-text="handleCheckText" />

      <analysis-history
          :refresh-trigger="historyRefreshTrigger"
          @item-deleted="handleHistoryItemDeleted"
      />
    </main>

    <footer>
      <p>© 2025 Correcteur IA - Tous droits réservés</p>
    </footer>
  </div>
</template>

