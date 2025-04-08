package com.coordia.atelier.Service.Impl;

import com.coordia.atelier.Entity.Correction;
import com.coordia.atelier.Repository.CorrectionRepository;
import com.coordia.atelier.Service.CorrectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CorrectionServiceImpl implements CorrectionService {
    @Autowired
    private CorrectionRepository correctionRepo;

    @Override
    public Correction saveCorrection(Correction correction) {
        return correctionRepo.save(correction);
    }

    @Override
    public List<Correction> getAllCorrections() {
        return (List<Correction>) correctionRepo.findAll();
    }

    @Override
    public Optional<Correction> getCorrectionById(Long id) {
        return correctionRepo.findById(id);
    }

    @Override
    public Correction updateCorrection(Correction correction, Long id) {
        Correction existingCorrection = correctionRepo.findById(id).orElseThrow(
                ()-> new RuntimeException()
        );
        // Mise à jour des attributs
        existingCorrection.setDetails(correction.getDetails());
        existingCorrection.setLogs(correction.getLogs());
        existingCorrection.setOriginalText(correction.getOriginalText());
        existingCorrection.setProcessedText(correction.getProcessedText());
        existingCorrection.setReadabilityScore(correction.getReadabilityScore());
        correctionRepo.save(existingCorrection);
        return existingCorrection;
    }

    @Override
    public void deleteCorrection(Long id) {
        correctionRepo.deleteById(id);
    }
}
