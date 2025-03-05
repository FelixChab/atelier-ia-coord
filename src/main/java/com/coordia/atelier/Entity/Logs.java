package com.coordia.atelier.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Logs {

    @Id
    @GeneratedValue
    private Long id;

    private String requestText;
    private String responseText;
    private boolean status;
    private String errorMessage; // message d'erreur si status = FAILED
    private Date timestamp; // date du traitement IA

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
