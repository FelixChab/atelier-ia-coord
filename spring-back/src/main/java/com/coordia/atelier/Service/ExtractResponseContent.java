package com.coordia.atelier.Service;

import com.coordia.atelier.Exception.AIServiceException;

import java.util.List;
import java.util.Map;

public class ExtractResponseContent {
    /**
     * Extracts the content from the OpenAI API response
     */
    private String extractResponseContent(Map responseBody) {
        try {
            if (responseBody != null && responseBody.containsKey("choices")) {
                List<Map<String, Object>> choices = (List<Map<String, Object>>) responseBody.get("choices");
                if (!choices.isEmpty()) {
                    Map<String, Object> choice = choices.get(0);
                    if (choice.containsKey("message")) {
                        Map<String, Object> message = (Map<String, Object>) choice.get("message");
                        if (message.containsKey("content")) {
                            return (String) message.get("content");
                        }
                    }
                }
            }
            throw new AIServiceException("Format de réponse OpenAI inattendu");
        } catch (Exception e) {
            // Log the error and throw a custom exception
            throw new AIServiceException("Impossible d'extraire le contenu de la réponse", e);
        }
    }
}
