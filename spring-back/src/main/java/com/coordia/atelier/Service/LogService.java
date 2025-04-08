package com.coordia.atelier.Service;

import com.coordia.atelier.Model.InteractionLog;
import com.coordia.atelier.Repository.LogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogService {

    private final LogRepository logRepository;

    /**
     * Enregistre une interaction avec l'IA
     */
    public void logInteraction(String request, String response, boolean success, String errorMessage) {
        InteractionLog log = new InteractionLog();
        log.setRequestPayload(request);
        log.setResponsePayload(response);
        log.setSuccess(success);
        log.setErrorMessage(errorMessage);
        log.setTimestamp(LocalDateTime.now());

        logRepository.save(log);
        log.info("Interaction enregistrée: success={}, longueur requête={}, longueur réponse={}",
                success, request.length(), response != null ? response.length() : 0);
    }

    /**
     * Récupère tous les logs triés par date (du plus récent au plus ancien)
     */
    public List<InteractionLog> getAllLogs() {
        return logRepository.findAllByOrderByTimestampDesc();
    }

    /**
     * Récupère les statistiques des interactions
     */
    public Map<String, Long> getStatistics() {
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", logRepository.count());
        stats.put("successful", logRepository.countBySuccessTrue());
        stats.put("failed", logRepository.countBySuccessFalse());
        return stats;
    }
}