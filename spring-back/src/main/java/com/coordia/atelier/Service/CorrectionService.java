package com.coordia.atelier.Service;

import com.coordia.atelier.Entity.Correction;

import java.util.List;
import java.util.Optional;

public interface CorrectionService {
    Correction saveCorrection(Correction correction);

    List<Correction> getAllCorrections();

    Optional<Correction> getCorrectionById(Long id);

    Correction updateCorrection(Correction correction, Long id);

    void deleteCorrection(Long id);
}
