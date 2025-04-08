package com.coordia.atelier.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorrectionRequest {

    @NotBlank(message = "Le texte ne peut pas être vide")
    @Size(min = 1, max = 10000, message = "Le texte doit contenir entre 1 et 10000 caractères")
    private String text;

    private boolean checkGrammar;
    private boolean checkSpelling;
    private boolean checkPunctuation;
    private boolean suggestSynonyms;
}

