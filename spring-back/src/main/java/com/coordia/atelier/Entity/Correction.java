package com.coordia.atelier.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "corrections")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Correction {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String originalText;

    @Column(columnDefinition = "TEXT")
    private String correctedText;

    @Column(columnDefinition = "TEXT")
    private String correctionDetails;

    private String status;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private int errorCount;

    private double qualityScore;
}