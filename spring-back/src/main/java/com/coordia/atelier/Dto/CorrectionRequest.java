package com.coordia.atelier.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CorrectionRequest {

    @NotBlank(message = "Le texte ne peut pas être vide")
    @Size(min = 1, max = 10000, message = "Le texte doit contenir entre 1 et 10000 caractères")
    private String text;

    @Getter
    private boolean checkGrammar = false;

    @Getter
    private boolean checkSpelling = false;

    @Getter
    private boolean checkPunctuation = false;

    @Getter
    private boolean suggestSynonyms = false;
}

