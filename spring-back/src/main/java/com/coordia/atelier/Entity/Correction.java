package com.coordia.atelier.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Correction {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private Details details;

    @OneToOne
    private Logs logs;

    private String originalText; // texte envoyé par l'utilisateur

    private String processedText; // texte corrigé par l'IA

    private int readabilityScore; // score global du texte

    public void setId(Long id) {
        this.id = id;
    }
    public void setDetails(Details detail) { this.details = detail; }
    public void setLogs(Logs log) { this.logs = log; }
    public void setOriginalText(String text) { this.originalText = text; }
    public void setProcessedText(String  text) { this.processedText = text; }
    public void setReadabilityScore(int score) { this.readabilityScore = score; }
}