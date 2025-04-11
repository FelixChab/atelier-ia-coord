<template>
  <div class="spellchecker-form">
    <h2>Correcteur d'orthographe et de grammaire</h2>
    <p class="subtitle">Perfectionnez vos textes grâce à notre IA de pointe</p>

    <div class="form-group">
      <textarea
        id="text-input"
        v-model="textInput"
        placeholder="Écrivez ou collez votre texte ici..."
        rows="8"
        :disabled="loading"
        @input="debouncedAnalyze"
      ></textarea>

      <div v-if="autoSaveStatus" class="auto-save-status">
        {{ autoSaveStatus }}
      </div>
    </div>

    <div class="check-options">
      <div class="options-container">
        <div class="checkbox-item">
          <input
            type="checkbox"
            id="check-grammar"
            v-model="options.checkGrammar"
          />
          <label for="check-grammar">Grammaire</label>
        </div>

        <div class="checkbox-item">
          <input
            type="checkbox"
            id="check-spelling"
            v-model="options.checkSpelling"
          />
          <label for="check-spelling">Orthographe</label>
        </div>

        <div class="checkbox-item">
          <input
            type="checkbox"
            id="check-punctuation"
            v-model="options.checkPunctuation"
          />
          <label for="check-punctuation">Ponctuation</label>
        </div>

        <div class="checkbox-item">
          <input
            type="checkbox"
            id="suggest-synonyms"
            v-model="options.suggestSynonyms"
          />
          <label for="suggest-synonyms">Synonymes</label>
        </div>
      </div>

      <button
        @click="checkText"
        :disabled="!isValid || loading"
        class="check-button"
      >
        {{ loading ? "Vérification..." : "Vérifier" }}
      </button>
    </div>

    <div v-if="error" class="error-message">
      <p>{{ error }}</p>
    </div>

    <div v-if="loading" class="loading-message">
      <p>Analyse en cours...</p>
    </div>
    <div v-if="!loading && result" class="result-section">
      <div class="result-header">
        <h3>Texte corrigé</h3>
        <div v-if="options.checkSpelling && options.checkGrammar" class="quality-indicator">
          <span class="quality-label">Qualité:</span>
          <div class="quality-dots">
            <span
              v-for="n in 10"
              :key="n"
              class="quality-dot"
              :class="{ active: n <= Math.ceil(result.qualityScore) }"
            ></span>
          </div>
        </div>
      </div>

      <div
        class="corrected-text"
        v-html="formatCorrectedText(result.correctedText)"
      ></div>

      <div
        v-if="result.correctionDetails && result.correctionDetails.length > 0"
        class="correction-details"
      >
        <h4>Corrections ({{ result.errorCount }})</h4>
        <ul class="corrections-list">
          <li
            v-for="(correction, index) in result.correctionDetails"
            :key="index"
            class="correction-item"
          >
            <div
              class="correction-type"
              :class="getCorrectionTypeClass(correction.type)"
            >
              {{ correction.type || "Correction" }}
            </div>
            <div class="correction-content">
              <span class="original">{{ correction.original }} →</span>
              <span class="explanation" v-if="correction.explanation">{{
                correction.explanation
              }}</span>
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed, onUnmounted } from "vue";
import "../assets/styles/spellcheckerForm.css";

export default {
  name: "SpellcheckerForm",
  emits: ["check-text"],
  setup(props, { emit }) {
    const textInput = ref("");
    const loading = ref(false);
    const error = ref(null);
    const result = ref(null);
    const autoSaveStatus = ref("");

    // Debounce timer reference
    let debounceTimer = null;

    const options = reactive({
      checkGrammar: true,
      checkSpelling: true,
      checkPunctuation: true,
      suggestSynonyms: false,
    });

    const isValid = computed(() => {
      return textInput.value.trim().length > 0;
    });

    // Function to analyze text with debounce
    function debouncedAnalyze() {
      // Clear any existing timer
      if (debounceTimer) {
        clearTimeout(debounceTimer);
        autoSaveStatus.value = "En attente...";
      }

      // Only proceed if there's valid text
      if (!isValid.value) {
        autoSaveStatus.value = "";
        return;
      }

      // Set a new timer
      debounceTimer = setTimeout(() => {
        autoSaveStatus.value = "Analyse en cours...";
        checkText(true); // Pass true to indicate this is an auto-save
      }, 2000); // 2 seconds delay
    }

    function checkText(isAutoSave = false) {
      if (!isValid.value) return;

      loading.value = true;
      error.value = null;

      try {
        // Prepare the payload
        const payload = {
          text: textInput.value,
          checkGrammar: options.checkGrammar,
          checkSpelling: options.checkSpelling,
          checkPunctuation: options.checkPunctuation,
          suggestSynonyms: options.suggestSynonyms,
          isAutoSave, // Add this flag to indicate if it's an auto-save
        };

        // Emit event to parent component to handle the API call
        emit("check-text", payload);

        if (isAutoSave) {
          // Update status for auto-save
          setTimeout(() => {
            autoSaveStatus.value = "Analyse automatique terminée";
            // Clear the status after a few seconds
            setTimeout(() => {
              autoSaveStatus.value = "";
            }, 3000);
          }, 500);
        }
      } catch (err) {
        console.error("Error in form submission:", err);
        error.value =
          "Une erreur est survenue lors de la vérification du texte.";
        loading.value = false;
        autoSaveStatus.value = "Échec de l'analyse automatique";
      }
    }

    function updateResult(newResult) {
      result.value = newResult;
      loading.value = false;
    }

    function setError(errorMessage) {
      error.value = errorMessage;
      loading.value = false;
      if (autoSaveStatus.value) {
        autoSaveStatus.value = "Échec de l'analyse automatique";
      }
    }

    // Clear any pending timers when component is unmounted
    onUnmounted(() => {
      if (debounceTimer) {
        clearTimeout(debounceTimer);
      }
    });

    // Other functions remain the same
    function formatCorrectedText(text) {
      if (!text) return "";
      return text.replace(/\*\*(.*?)\*\*/g, "<strong>$1</strong>");
    }

    function getCorrectionTypeClass(type) {
      if (!type) return "";

      const typeMap = {
        Grammaire: "grammar",
        Orthographe: "spelling",
        Ponctuation: "punctuation",
        Synonyme: "synonym",
      };

      return typeMap[type] || "";
    }

    return {
      textInput,
      options,
      loading,
      error,
      result,
      autoSaveStatus,
      isValid,
      checkText,
      debouncedAnalyze,
      updateResult,
      setError,
      formatCorrectedText,
      getCorrectionTypeClass,
    };
  },
};
</script>
