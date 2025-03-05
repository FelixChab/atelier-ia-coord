package com.coordia.atelier.Entity;

import com.coordia.atelier.Enum.Severities;
import com.coordia.atelier.Enum.Types;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Details {
    @Id
    @GeneratedValue
    private Long id;

    private Types type;
    private String error;
    private String suggestion;
    private Severities severity;

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}