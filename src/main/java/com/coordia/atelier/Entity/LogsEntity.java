package com.atelier.coord.Entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogsEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String requestText; // contenu envoyé à l'IA
    private String responseText; // contenu généré par l'IA

    private boolean status; // SUCCESS ou FAILED (true ou false)
    private String errorMessage; // message d'erreur si status = FAILED
    private Date timestamp; // date du traitement IA
}
