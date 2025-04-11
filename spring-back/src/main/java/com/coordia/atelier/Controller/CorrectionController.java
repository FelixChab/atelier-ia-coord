package com.coordia.atelier.Controller;

import com.coordia.atelier.Dto.CorrectionRequest;
import com.coordia.atelier.Dto.CorrectionResponse;
import com.coordia.atelier.Service.CorrectionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/corrections")
@CrossOrigin(origins = "*")
public class CorrectionController {

    private final CorrectionService correctionService;

    public CorrectionController(CorrectionService correctionService) {
        this.correctionService = correctionService;
    }

    @PostMapping
    public ResponseEntity<CorrectionResponse> correctText(@Valid @RequestBody CorrectionRequest request) {
        CorrectionResponse response = correctionService.correctText(request);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint pour récupérer une correction par son ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<CorrectionResponse> getCorrectionById(@PathVariable UUID id) {
        CorrectionResponse response = correctionService.getCorrectionById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Endpoint pour récupérer l'historique des corrections
     */
    @GetMapping
    public ResponseEntity<Object> getCorrectionHistory() {
        List<CorrectionResponse> history = correctionService.getCorrectionHistory();
        return ResponseEntity.ok(history);
    }

    /**
     * Endpoint pour rechercher des corrections par texte
     */
    @GetMapping("/search")
    public ResponseEntity<Object> searchCorrections(@RequestParam String query) {
        List<CorrectionResponse> results = correctionService.searchCorrections(query);
        return ResponseEntity.ok(results);
    }

    @DeleteMapping("/history/{id}")
    public ResponseEntity<Void> deleteCorrection(@PathVariable UUID id) {
        correctionService.deleteCorrection(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/history")
    public ResponseEntity<Void> deleteAllCorrections() {
        correctionService.deleteAllCorrections();
        return ResponseEntity.noContent().build();
    }
}
