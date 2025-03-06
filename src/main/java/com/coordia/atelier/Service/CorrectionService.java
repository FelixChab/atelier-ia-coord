package com.coordia.atelier.Service;

import com.coordia.atelier.Dto.CorrectionRequest;
import com.coordia.atelier.Dto.CorrectionResponse;
import com.coordia.atelier.Dto.CorrectionResponse.CorrectionDetail;
import com.coordia.atelier.Dto.CorrectionResponse.CorrectionStatus;
import com.coordia.atelier.Dto.CorrectionResponse.CorrectionType;
import com.coordia.atelier.Exception.CorrectionException;
import com.coordia.atelier.Model.Correction;
import com.coordia.atelier.Repository.CorrectionRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class CorrectionService {

    private final CorrectionRepository correctionRepository;
    private final CorrectionAIService correctionAIService;
    private final ObjectMapper objectMapper;

    @Transactional
    public CorrectionResponse correctText(CorrectionRequest request) {
        String originalText = request.getText();
        log.info("Nouvelle demande de correction pour un texte de {} caractères", originalText.length());

        try {
            // Appel au service IA pour la correction
            String aiResponse = correctionAIService.corrigerTexte(originalText);

            // Extraction du texte corrigé et des détails de correction
            String correctedText = extractCorrectedText(aiResponse);
            List<CorrectionDetail> correctionDetails = extractCorrectionDetails(aiResponse);

            // Calcul du nombre d'erreurs et du score de qualité
            int errorCount = correctionDetails.size();
            double qualityScore = calculateQualityScore(originalText, errorCount);

            log.info("Texte corrigé avec {} erreurs trouvées, score de qualité: {}", errorCount, qualityScore);

            // Création de l'entité Correction pour la base de données
            Correction correction = new Correction();
            correction.setOriginalText(originalText);
            correction.setCorrectedText(correctedText);
            correction.setCorrectionDetails(serializeCorrectionDetails(correctionDetails));
            correction.setStatus(CorrectionStatus.SUCCESS.name());
            correction.setCreatedAt(LocalDateTime.now());
            correction.setErrorCount(errorCount);
            correction.setQualityScore(qualityScore);

            // Sauvegarde en base de données
            Correction savedCorrection = correctionRepository.save(correction);
            log.info("Correction sauvegardée en base avec l'ID: {}", savedCorrection.getId());

            // Construction de la réponse
            return CorrectionResponse.builder()
                    .id(savedCorrection.getId())
                    .originalText(originalText)
                    .correctedText(correctedText)
                    .correctionDetails(correctionDetails)
                    .status(CorrectionStatus.SUCCESS)
                    .createdAt(savedCorrection.getCreatedAt())
                    .errorCount(errorCount)
                    .qualityScore(qualityScore)
                    .build();

        } catch (Exception e) {
            log.error("Erreur lors de la correction du texte", e);

            // En cas d'erreur, on retourne une réponse avec le statut FAILED
            return CorrectionResponse.builder()
                    .originalText(originalText)
                    .correctedText(originalText) // On retourne le texte original
                    .correctionDetails(new ArrayList<>())
                    .status(CorrectionStatus.FAILED)
                    .createdAt(LocalDateTime.now())
                    .errorCount(0)
                    .qualityScore(0)
                    .build();
        }
    }

    public CorrectionResponse getCorrectionById(UUID id) {
        Optional<Correction> correctionOpt = correctionRepository.findById(id);

        if (correctionOpt.isPresent()) {
            Correction correction = correctionOpt.get();
            List<CorrectionDetail> details = deserializeCorrectionDetails(correction.getCorrectionDetails());

            return CorrectionResponse.builder()
                    .id(correction.getId())
                    .originalText(correction.getOriginalText())
                    .correctedText(correction.getCorrectedText())
                    .correctionDetails(details)
                    .status(CorrectionStatus.valueOf(correction.getStatus()))
                    .createdAt(correction.getCreatedAt())
                    .errorCount(correction.getErrorCount())
                    .qualityScore(correction.getQualityScore())
                    .build();
        } else {
            throw new CorrectionException("Correction non trouvée avec l'ID: " + id);
        }
    }

    /**
     * Récupère l'historique des corrections
     */
    public List<CorrectionResponse> getCorrectionHistory() {
        List<Correction> corrections = correctionRepository.findAllByOrderByCreatedAtDesc();
        List<CorrectionResponse> responses = new ArrayList<>();

        for (Correction correction : corrections) {
            List<CorrectionDetail> details = deserializeCorrectionDetails(correction.getCorrectionDetails());

            responses.add(CorrectionResponse.builder()
                    .id(correction.getId())
                    .originalText(correction.getOriginalText())
                    .correctedText(correction.getCorrectedText())
                    .correctionDetails(details)
                    .status(CorrectionStatus.valueOf(correction.getStatus()))
                    .createdAt(correction.getCreatedAt())
                    .errorCount(correction.getErrorCount())
                    .qualityScore(correction.getQualityScore())
                    .build());
        }

        return responses;
    }

    /**
     * Recherche des corrections par texte
     */
    public List<CorrectionResponse> searchCorrections(String searchText) {
        List<Correction> corrections = correctionRepository.findByOriginalTextContainingIgnoreCase(searchText);
        List<CorrectionResponse> responses = new ArrayList<>();

        for (Correction correction : corrections) {
            List<CorrectionDetail> details = deserializeCorrectionDetails(correction.getCorrectionDetails());

            responses.add(CorrectionResponse.builder()
                    .id(correction.getId())
                    .originalText(correction.getOriginalText())
                    .correctedText(correction.getCorrectedText())
                    .correctionDetails(details)
                    .status(CorrectionStatus.valueOf(correction.getStatus()))
                    .createdAt(correction.getCreatedAt())
                    .errorCount(correction.getErrorCount())
                    .qualityScore(correction.getQualityScore())
                    .build());
        }

        return responses;
    }

    /**
     * Extrait le texte corrigé de la réponse de l'IA
     */
    private String extractCorrectedText(String aiResponse) {
        // Logique d'extraction du texte corrigé
        // Cette implémentation dépend du format de réponse de votre IA

        // Exemple simple: on suppose que le texte corrigé est la première partie de la réponse
        // jusqu'à une ligne vide ou un marqueur spécifique
        String[] parts = aiResponse.split("\n\n", 2);
        if (parts.length > 0) {
            return parts[0].trim();
        }
        return aiResponse; // Fallback si on ne peut pas extraire
    }

    /**
     * Extrait les détails des corrections de la réponse de l'IA
     */
    private List<CorrectionDetail> extractCorrectionDetails(String aiResponse) {
        List<CorrectionDetail> details = new ArrayList<>();

        // Logique d'extraction des détails de correction
        // Cette implémentation dépend du format de réponse de votre IA

        // Exemple: on cherche des patterns comme "- "mot" → "correction": explication"
        String[] parts = aiResponse.split("\n\n", 2);
        if (parts.length > 1) {
            String correctionsPart = parts[1];

            // Regex pour extraire les corrections (à adapter selon le format de votre IA)
            Pattern pattern = Pattern.compile("- \\\"([^\\\"]+)\\\" → \\\"([^\\\"]+)\\\"(?:: ([^\\n]+))?");
            Matcher matcher = pattern.matcher(correctionsPart);

            int position = 0;
            while (matcher.find()) {
                String originalWord = matcher.group(1);
                String correctedWord = matcher.group(2);
                String explanation = matcher.groupCount() > 2 && matcher.group(3) != null ?
                        matcher.group(3) : "Correction suggérée";

                // Déterminer le type de correction (simpliste)
                CorrectionType type = determineCorrectType(originalWord, correctedWord, explanation);

                details.add(CorrectionDetail.builder()
                        .originalWord(originalWord)
                        .correctedWord(correctedWord)
                        .explanation(explanation)
                        .type(type)
                        .position(position++)
                        .build());
            }
        }

        return details;
    }

    /**
     * Détermine le type de correction
     */
    private CorrectionType determineCorrectType(String original, String corrected, String explanation) {
        String lowerExplanation = explanation.toLowerCase();

        if (lowerExplanation.contains("orthographe") || lowerExplanation.contains("spelling")) {
            return CorrectionType.SPELLING;
        } else if (lowerExplanation.contains("grammaire") || lowerExplanation.contains("grammar")) {
            return CorrectionType.GRAMMAR;
        } else if (lowerExplanation.contains("ponctuation") || lowerExplanation.contains("punctuation")) {
            return CorrectionType.PUNCTUATION;
        } else if (lowerExplanation.contains("syntaxe") || lowerExplanation.contains("syntax")) {
            return CorrectionType.SYNTAX;
        } else {
            return CorrectionType.STYLE;
        }
    }

    /**
     * Calcule un score de qualité basé sur le nombre d'erreurs et la longueur du texte
     */
    private double calculateQualityScore(String text, int errorCount) {
        int wordCount = text.split("\\s+").length;

        if (wordCount == 0) return 0;

        // Calcul du taux d'erreur par mot
        double errorRate = (double) errorCount / wordCount;

        // Conversion en score de qualité (10 - taux d'erreur * 100)
        // Limité entre 0 et 10
        double score = 10 - (errorRate * 100);
        return Math.max(0, Math.min(10, score));
    }

    /**
     * Sérialise la liste des détails de correction en JSON pour stockage en base
     */
    private String serializeCorrectionDetails(List<CorrectionDetail> details) {
        try {
            return objectMapper.writeValueAsString(details);
        } catch (JsonProcessingException e) {
            log.error("Erreur lors de la sérialisation des détails de correction", e);
            return "[]";
        }
    }

    /**
     * Désérialise les détails de correction depuis le JSON stocké en base
     */
    private List<CorrectionDetail> deserializeCorrectionDetails(String detailsJson) {
        try {
            if (detailsJson == null || detailsJson.isEmpty()) {
                return new ArrayList<>();
            }
            return objectMapper.readValue(detailsJson, new TypeReference<List<CorrectionDetail>>() {});
        } catch (JsonProcessingException e) {
            log.error("Erreur lors de la désérialisation des détails de correction", e);
            return new ArrayList<>();
        }
    }
}