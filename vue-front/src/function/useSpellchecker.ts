import { ref, reactive } from 'vue'
import axios from 'axios'

export function useSpellchecker() {
    const text = ref('')
    const result = ref(null)
    const loading = ref(false)
    const error = ref(null)

    const options = reactive({
        checkGrammar: true,
        checkSpelling: true,
        checkPunctuation: true,
        suggestSynonyms: false
    })

    async function checkText() {
        if (!text.value.trim()) return

        loading.value = true
        error.value = null

        try {
            const payload = {
                text: text.value,
                parameters: { ...options }
            }

            const response = await axios.post('/api/analysis/french', payload)
            result.value = response.data
        } catch (err) {
            console.error('Analysis error:', err)
            error.value = err.response?.data?.message ||
                'Une erreur est survenue lors de l\'analyse. Veuillez réessayer.'
        } finally {
            loading.value = false
        }
    }

    return {
        text,
        result,
        loading,
        error,
        options,
        checkText
    }
}