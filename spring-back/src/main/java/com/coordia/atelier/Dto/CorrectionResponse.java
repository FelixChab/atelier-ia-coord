package com.coordia.atelier.Dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CorrectionResponse {

    private UUID id;
    private String originalText;
    private String correctedText;
    private List<CorrectionDetail> correctionDetails;
    private CorrectionStatus status;
    private LocalDateTime createdAt;
    private int errorCount;
    private double qualityScore;

    // Classe imbriquée pour les détails de correction
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CorrectionDetail {
        private String originalWord;
        private String correctedWord;
        private String explanation;
        private CorrectionType type;
        private int position;
    }

    // Énumération pour le type de correction
    public enum CorrectionType {
        SPELLING,
        GRAMMAR,
        PUNCTUATION,
        SYNTAX,
        STYLE
    }

    // Énumération pour le statut de la correction
    public enum CorrectionStatus {
        SUCCESS,
        PARTIAL_SUCCESS,
        FAILED
    }
}