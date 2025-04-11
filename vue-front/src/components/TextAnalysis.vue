<script>
import { mapActions, mapGetters } from "vuex";
import "@/assets/styles/textAnalysis.css";
export default {
  name: "TextAnalysis",
  data() {
    return {
      userText: "",
      analysisType: "age",
    };
  },
  computed: {
    ...mapGetters("analysis", [
      "getAnalysisResult",
      "isLoading",
      "hasError",
      "getError",
    ]),
    result() {
      return this.getAnalysisResult;
    },
    loading() {
      return this.isLoading;
    },
    error() {
      return this.hasError ? this.getError : null;
    },
  },
  methods: {
    ...mapActions("analysis", ["analyzeAge", "analyzeFrench"]),
    async analyzeText() {
      if (!this.userText) return;

      try {
        if (this.analysisType === "age") {
          await this.analyzeAge(this.userText);
        } else if (this.analysisType === "french") {
          await this.analyzeFrench(this.userText);
        }
      } catch (error) {
        console.error("Analysis failed:", error);
      }
    },
  },
};
</script>

<template>
  <div class="analysis-container">
    <h1>AI Text Analysis</h1>

    <div class="analysis-options">
      <button
          :class="{ active: analysisType === 'age' }"
          @click="analysisType = 'age'"
      >
        Age Analysis
      </button>
      <button
          :class="{ active: analysisType === 'french' }"
          @click="analysisType = 'french'"
      >
        French Quality Analysis
      </button>
    </div>

    <div class="input-section">
      <textarea
          v-model="userText"
          placeholder="Enter text for analysis..."
          rows="6"
      ></textarea>

      <button
          @click="analyzeText"
          :disabled="!userText || loading"
          class="analyze-btn"
      >
        {{ loading ? "Analyzing..." : "Analyze Text" }}
      </button>
    </div>

    <div v-if="result" class="result-section">
      <h2>Analysis Result</h2>
      <div class="result-card">
        <template v-if="analysisType === 'age'">
          <h3>Estimated Age: {{ result.estimatedAge }}</h3>
          <div class="confidence">Confidence: {{ result.confidence }}%</div>
        </template>

        <template v-else-if="analysisType === 'french'">
          <h3>Language Quality Score: {{ result.qualityScore }}/10</h3>
          <div class="feedback">
            <h4>Feedback:</h4>
            <p>{{ result.feedback }}</p>
          </div>
        </template>
      </div>
    </div>

    <div v-if="error" class="error-message">
      {{ error }}
    </div>
  </div>
</template>
