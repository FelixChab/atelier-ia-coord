package com.coordia.atelier.Service;

import com.coordia.atelier.Exception.AIServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CorrectionAIService {

    private final FileReadingService fileReadingService;

    private final LogService logService;
    @Value("${spring.ai.openai.api-key}")
    private String apiKey;
    private final RestTemplate restTemplate = new RestTemplate();
    private final String OPENAI_API_URL = "https://api.openai.com/v1/responses";

    public String corrigerTexte(String texteOriginal, boolean checkGrammar, boolean checkSpelling, boolean checkPunctuation, boolean suggestSynonyms) {

        try {
            log.info("Demande de correction pour un texte de {} caractères", texteOriginal.length());

            // Lecture du prompt de correction orthographique
            String promptTemplate = fileReadingService.readInternalFileAsString("prompts/promptCorrecteur.txt");

            // Ajouter des instructions pour une réponse détaillée
            promptTemplate = enhancePromptForDetailedResponse(promptTemplate, texteOriginal);

            log.info("CheckGrammar: {}, CheckSpelling: {}, CheckPunctuation: {}, SuggestSynonyms: {}",
                    checkGrammar, checkSpelling, checkPunctuation, suggestSynonyms);
            // Vérification des booléens
            if (checkGrammar) {
                promptTemplate += "\n\n# INSTRUCTION\n" +
                        "First present the fully corrected grammar version\n" +
                        "Then lists all the changes with explanations:\n" +
                        " - \"original word/phrase\". → \"correction\": concise explanation";
            }
            if (checkSpelling) {
                promptTemplate += "\n\n# INSTRUCTION\n" +
                        "First present the fully corrected orthography version\n" +
                        "Then lists all the changes with explanations:\n" +
                        " - \"original word/phrase\". → \"correction\": concise explanation";
            }
            if (checkPunctuation) {
                promptTemplate += "\n\n# INSTRUCTION\n" +
                        "First present the fully corrected punctuation version\n" +
                        "Then lists all the changes with explanations:\n" +
                        " - \"original word/phrase\". → \"correction\": concise explanation";
            }
            if (suggestSynonyms) {
                promptTemplate += "\n\n# INSTRUCTION\n" +
                        "First present one or more synonym of the text\n" +
                        "Then lists all the synonym with explanations:\n" +
                        " - \"original word/phrase\". → \"synonym\": concise explanation";
            }

            log.info("Prompt de correction : {}", promptTemplate);

            // Préparation de la requête pour OpenAI
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "gpt-4o");

            List<Map<String, String>> input = new ArrayList<>();

            // Ajouter le message système (instructions)
            Map<String, String> systemMessage = new HashMap<>();
            systemMessage.put("role", "system");
            systemMessage.put("content", promptTemplate);
            input.add(systemMessage);

            // Ajouter le message utilisateur (texte à corriger)
            Map<String, String> userMessage = new HashMap<>();
            userMessage.put("role", "user");
            userMessage.put("content", texteOriginal);
            input.add(userMessage);

            requestBody.put("input", input);

            // Configuration des en-têtes HTTP
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);

            // Création de l'entité HTTP
            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            // Appel à l'API OpenAI
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    OPENAI_API_URL, entity, Map.class);

            // Extraction de la réponse
            String texteCorrige = extractResponseContent(response.getBody());


            // Enregistrement de l'interaction réussie
            logService.logInteraction(texteOriginal, texteCorrige, true, null);

            log.info("Correction réussie, réponse de {} caractères", texteCorrige.length());
            return texteCorrige;
        }
        catch (Exception e) {
            log.error("Erreur lors de la correction du texte", e);
            // Enregistrement de l'interaction échouée
            logService.logInteraction(texteOriginal, null, false, e.getMessage());
            throw new AIServiceException("Erreur lors de la correction du texte", e);
        }
    }

    /**
     * Enhances the prompt to request a detailed response
     */
    private String enhancePromptForDetailedResponse(String originalPrompt, String texteOriginal) {
        StringBuilder enhancedPrompt = new StringBuilder(originalPrompt);

        // Calculate the minimum expected response length
        int minResponseLength = Math.max(texteOriginal.length() * 2, 500); // At least 2x the original or 500 chars

        // Add instructions for detailed response
        enhancedPrompt.append("\nIMPORTANT: Your answer must be detailed and complete. For each correction, provide an in-depth explanation that includes:");
        enhancedPrompt.append("\n1. The grammatical or spelling rule applied");
        enhancedPrompt.append("\n2. Why the correction is necessary");
        enhancedPrompt.append("\n3. Similar examples if relevant");

        enhancedPrompt.append("\n\nFor complex errors, develop your explanation further. ");
        enhancedPrompt.append("If the text contains few errors, add some general advice on how to improve your writing.");

        enhancedPrompt.append("\n\nIdeally, your answer should contain no more than ").append(minResponseLength).append(" characters, ");
        enhancedPrompt.append("with an “Overall analysis” section at the end to assess the overall quality of the text.");

        return enhancedPrompt.toString();
    }

    private String extractResponseContent(Map responseBody) {
        log.info("Extraction du contenu de la réponse OpenAI : {}", responseBody);
        try {
            if (responseBody != null && responseBody.containsKey("output")) {
                List<Map<String, Object>> response = (List<Map<String, Object>>) responseBody.get("output");
                if (!response.isEmpty()) {
                    Map<String, Object> output = response.get(0);
                    if (output.containsKey("content")) {
                        List<Map<String, Object>> Content = (List<Map<String, Object>>) output.get("content");
                        Map<String, Object> content = Content.get(0);
                        if (content.containsKey("text")) {
                            return (String) content.get("text");
                        }
                    }
                }
            }
            throw new AIServiceException("Format de réponse OpenAI inattendu");
        } catch (Exception e) {
            log.error("Erreur lors de l'extraction du contenu de la réponse", e);
            throw new AIServiceException("Impossible d'extraire le contenu de la réponse", e);
        }
    }
}