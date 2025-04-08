package com.coordia.atelier.Controller;

import com.coordia.atelier.Entity.Correction;
import com.coordia.atelier.Service.CorrectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/corrections")
public class CorrectionController {

    @Autowired
    private CorrectionService correctionService;

    @PostMapping()
    public Correction saveCorrection(@RequestBody Correction correction) {
        return correctionService.saveCorrection(correction);
    }

    @GetMapping
    public List<Correction> getAllCorrection() {
        return correctionService.getAllCorrections();
    }

    @GetMapping("/{id}")
    public Optional<Correction> getCorrectionById(@PathVariable("id") Long id) {
        return correctionService.getCorrectionById(id);
    }

    @PutMapping("/{id}")
    public Correction updateCorrection(@RequestBody Correction correction, @PathVariable("id") Long id) {
        return correctionService.updateCorrection(correction, id);
    }

    @DeleteMapping("/{id}")
    public void deleteCorrection(@PathVariable("id") Long id) {
        correctionService.deleteCorrection(id);
    }

}