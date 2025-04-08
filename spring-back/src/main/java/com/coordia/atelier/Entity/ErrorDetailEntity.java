package com.coordia.atelier.Entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ErrorDetailEntity {
    private String type;         // orthographe, grammaire, syntaxe..
    private String error;        // mot ou phrase incorrect
    private String suggestion;
    private String severity;
}