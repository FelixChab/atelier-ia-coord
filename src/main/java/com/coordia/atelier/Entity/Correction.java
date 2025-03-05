package com.coordia.atelier.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
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

    public Long getId() {
        return id;
    }

    // TODO

}
