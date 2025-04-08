package com.coordia.atelier.Repository;

import com.coordia.atelier.Model.Correction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface CorrectionRepository extends CrudRepository<Correction, UUID> {

    List<Correction> findAllByOrderByCreatedAtDesc();

    List<Correction> findByQualityScoreGreaterThanEqual(double minScore);

    List<Correction> findByErrorCountLessThanEqual(int maxErrors);

    List<Correction> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    List<Correction> findByOriginalTextContainingIgnoreCase(String searchText);
}