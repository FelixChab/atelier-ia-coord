<script>
import { mapActions, mapGetters } from "vuex";

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
