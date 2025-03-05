package com.coordia.atelier.Service;

import com.coordia.atelier.Entity.Logs;

import java.util.List;
import java.util.Optional;

public interface LogsService {
    Logs saveLogs(Logs logs);

    List<Logs> getAllLogs();

    Optional<Logs> getLogsById(Long id);

    Logs updateLogs(Logs log, Long id);

    void deleteLogs(Long id);
}
