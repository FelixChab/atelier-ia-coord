package com.coordia.atelier.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Logs {

    @Getter
    @Id
    @GeneratedValue
    private Long id;

    private String requestText;

    private String responseText;

    private boolean status;

    private String errorMessage; // Message d'erreur si status = FAILED

    private Date timestamp; // date du traitement IA

    public void setId(Long id) {
        this.id = id;
    }
    public void setRequestText(String reqText) { this.requestText = reqText; }
    public void setResponseText(String respText) { this.responseText = respText; }
    public void setStatus(boolean status) { this.status = status; }
    public void setErrorMessage(String errorMess) { this.errorMessage = errorMess; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
}