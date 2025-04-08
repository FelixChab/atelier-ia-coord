

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
      ></textarea>
    </div>

    <div class="check-options">
      <div class="options-container">
        <div class="checkbox-item">
          <input
              type="checkbox"
              id="check-grammar"
              v-model="options.checkGrammar"
          >
          <label for="check-grammar">Grammaire</label>
        </div>

        <div class="checkbox-item">
          <input
              type="checkbox"
              id="check-spelling"
              v-model="options.checkSpelling"
          >
          <label for="check-spelling">Orthographe</label>
        </div>

        <div class="checkbox-item">
          <input
              type="checkbox"
              id="check-punctuation"
              v-model="options.checkPunctuation"
          >
          <label for="check-punctuation">Ponctuation</label>
        </div>

        <div class="checkbox-item">
          <input
              type="checkbox"
              id="suggest-synonyms"
              v-model="options.suggestSynonyms"
          >
          <label for="suggest-synonyms">Synonymes</label>
        </div>
      </div>

      <button
          @click="checkText"
          :disabled="!isValid || loading"
          class="check-button"
      >
        {{ loading ? 'Vérification...' : 'Vérifier' }}
      </button>
    </div>

    <div v-if="error" class="error-message">
      <p>{{ error }}</p>
    </div>

    <div v-if="result" class="result-section">
      <div class="result-header">
        <h3>Texte corrigé</h3>
        <div class="quality-indicator">
          <span class="quality-label">Qualité:</span>
          <div class="quality-dots">
            <span
                v-for="n in 5"
                :key="n"
                class="quality-dot"
                :class="{ active: n <= Math.ceil(result.qualityScore / 2) }"
            ></span>
          </div>
        </div>
      </div>

      <div class="corrected-text" v-html="formatCorrectedText(result.correctedText)"></div>

      <div v-if="result.correctionDetails && result.correctionDetails.length > 0" class="correction-details">
        <h4>Corrections ({{ result.errorCount }})</h4>
        <ul class="corrections-list">
          <li v-for="(correction, index) in result.correctionDetails" :key="index" class="correction-item">
            <div class="correction-type" :class="getCorrectionTypeClass(correction.type)">
              {{ correction.type || 'Correction' }}
            </div>
            <div class="correction-content">
              <span class="original">{{ correction.original }}</span> →
              <span class="corrected">{{ correction.corrected }}</span>
            </div>
            <div v-if="correction.explanation" class="correction-explanation">
              {{ correction.explanation }}
            </div>
          </li>
        </ul>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed } from 'vue'

export default {
  name: 'SpellcheckerForm',
  emits: ['check-text'],
  setup(props, { emit }) {
    const textInput = ref('')
    const loading = ref(false)
    const error = ref(null)
    const result = ref(null)

    const options = reactive({
      checkGrammar: true,
      checkSpelling: true,
      checkPunctuation: true,
      suggestSynonyms: false
    })

    const isValid = computed(() => {
      return textInput.value.trim().length > 0
    })

    function checkText() {
      if (!isValid.value) return

      loading.value = true
      error.value = null

      try {
        // Prepare the payload
        const payload = {
          text: textInput.value,
          parameters: {
            ...options
          }
        }

        // Emit event to parent component to handle the API call
        emit('check-text', payload)

      } catch (err) {
        console.error('Error in form submission:', err)
        error.value = 'Une erreur est survenue lors de la vérification du texte.'
        loading.value = false
      }
    }

    function updateResult(newResult) {
      result.value = newResult
      loading.value = false
    }

    function setError(errorMessage) {
      error.value = errorMessage
      loading.value = false
    }

    function formatCorrectedText(text) {
      if (!text) return ''
      // The corrected text contains markdown-style formatting, so we'll render it as HTML
      return text.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>')
    }

    function getCorrectionTypeClass(type) {
      if (!type) return ''

      const typeMap = {
        'Grammaire': 'grammar',
        'Orthographe': 'spelling',
        'Ponctuation': 'punctuation',
        'Synonyme': 'synonym'
      }

      return typeMap[type] || ''
    }

    return {
      textInput,
      options,
      loading,
      error,
      result,
      isValid,
      checkText,
      updateResult,
      setError,
      formatCorrectedText,
      getCorrectionTypeClass
    }
  }
}
</script>

<style scoped>
.spellchecker-form {
  background-color: white;
  border-radius: 10px;
  padding: 30px;
  box-shadow: 0 2px 15px rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
}

h2 {
  color: #3f51b5;
  margin-bottom: 10px;
  font-size: 1.8rem;
  text-align: center;
}

.subtitle {
  text-align: center;
  color: #757575;
  margin-bottom: 25px;
}

.form-group {
  margin-bottom: 20px;
}

textarea {
  width: 100%;
  padding: 15px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 16px;
  resize: vertical;
  transition: border-color 0.3s;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
}

textarea:focus {
  outline: none;
  border-color: #3f51b5;
  box-shadow: 0 0 0 2px rgba(63, 81, 181, 0.2);
}

.check-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.options-container {
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
}

.checkbox-item {
  display: flex;
  align-items: center;
}

.checkbox-item input {
  margin-right: 8px;
}

.check-button {
  background-color: #3f51b5;
  color: white;
  border: none;
  padding: 12px 30px;
  font-size: 16px;
  border-radius: 30px;
  cursor: pointer;
  transition: background-color 0.3s;
  font-weight: 600;
}

.check-button:hover {
  background-color: #303f9f;
}

.check-button:disabled {
  background-color: #bdbdbd;
  cursor: not-allowed;
}

.error-message {
  background-color: #ffebee;
  color: #c62828;
  padding: 15px;
  border-radius: 8px;
  margin-bottom: 20px;
}

.result-section {
  background-color: #f5f7ff;
  border-radius: 8px;
  padding: 20px;
  margin-top: 25px;
}

.result-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
}

h3 {
  color: #3f51b5;
  font-size: 1.4rem;
  margin: 0;
}

.quality-indicator {
  display: flex;
  align-items: center;
}

.quality-label {
  margin-right: 10px;
  color: #757575;
}

.quality-dots {
  display: flex;
  gap: 5px;
}

.quality-dot {
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background-color: #e0e0e0;
}

.quality-dot.active {
  background-color: #4caf50;
}

.corrected-text {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
  border-left: 4px solid #4caf50;
  margin-bottom: 20px;
  line-height: 1.6;
}

.correction-details {
  background-color: white;
  padding: 20px;
  border-radius: 8px;
}

h4 {
  color: #3f51b5;
  margin-bottom: 15px;
  font-size: 1.1rem;
}

.corrections-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.correction-item {
  padding: 12px 0;
  border-bottom: 1px solid #e0e0e0;
}

.correction-item:last-child {
  border-bottom: none;
}

.correction-type {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 0.8rem;
  margin-bottom: 8px;
  background-color: #e0e0e0;
  color: #424242;
}

.correction-type.grammar {
  background-color: #e3f2fd;
  color: #1565c0;
}

.correction-type.spelling {
  background-color: #e8f5e9;
  color: #2e7d32;
}

.correction-type.punctuation {
  background-color: #fff3e0;
  color: #e65100;
}

.correction-type.synonym {
  background-color: #f3e5f5;
  color: #7b1fa2;
}

.correction-content {
  margin-bottom: 8px;
}

.original {
  color: #e53935;
  text-decoration: line-through;
}

.corrected {
  color: #43a047;
  font-weight: 600;
}

.correction-explanation {
  font-size: 0.9rem;
  color: #757575;
  font-style: italic;
}

@media (max-width: 768px) {
  .spellchecker-form {
    padding: 20px;
  }

  .check-options {
    flex-direction: column;
    align-items: stretch;
  }

  .options-container {
    margin-bottom: 15px;
  }

  .check-button {
    width: 100%;
  }

  .result-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .quality-indicator {
    margin-top: 10px;
  }
}
</style>
