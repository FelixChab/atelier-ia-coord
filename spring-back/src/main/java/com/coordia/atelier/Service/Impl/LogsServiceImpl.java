package com.coordia.atelier.Service.Impl;

import com.coordia.atelier.Entity.Logs;
import com.coordia.atelier.Repository.LogsRepository;
import com.coordia.atelier.Service.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogsServiceImpl implements LogsService {
    @Autowired
    private LogsRepository logsRepo;

    @Override
    public Logs saveLogs(Logs log) {
        return logsRepo.save(log);
    }

    @Override
    public List<Logs> getAllLogs() {
        return (List<Logs>) logsRepo.findAll();
    }

    @Override
    public Optional<Logs> getLogsById(Long id) {
        return logsRepo.findById(id);
    }

    @Override
    public Logs updateLogs(Logs log, Long id) {
        Logs existingLogs = logsRepo.findById(id).orElseThrow(
                ()-> new RuntimeException()
        );
        // Mise à jour des attributs
        existingLogs.setTimestamp(log.getTimestamp());
        existingLogs.setStatus(log.isStatus());
        existingLogs.setErrorMessage(log.getErrorMessage());
        existingLogs.setRequestText(log.getRequestText());
        existingLogs.setResponseText(log.getResponseText());
        logsRepo.save(existingLogs);
        return existingLogs;
    }

    @Override
    public void deleteLogs(Long id) {
        logsRepo.deleteById(id);
    }
}
