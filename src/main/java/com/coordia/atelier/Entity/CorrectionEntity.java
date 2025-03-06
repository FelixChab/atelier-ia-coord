package com.coordia.atelier.Entity;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CorrectionEntity {
    @Id
    @GeneratedValue
    private Long id;
    private String originalText; // texte envoyé par l'utilisateur
    private String processedText; // texte corrigé par l'IA

    @ElementCollection
    private List<com.coordia.atelier.Entity.ErrorDetailEntity> errorDetails; // infos erreurs
    private int readabilityScore; // score global du texte

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    // TODO

}

