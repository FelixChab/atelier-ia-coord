package com.coordia.atelier.Controller;

import com.coordia.atelier.Entity.Logs;
import com.coordia.atelier.Service.LogsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logs")
public class LogsController {

    @Autowired
    private LogsService logsService;

    @PostMapping()
    public Logs saveLogs(@RequestBody Logs log) {
        return logsService.saveLogs(log);
    }

    @GetMapping
    public List<Logs> getAllLogs() {
        return logsService.getAllLogs();
    }

    @GetMapping("/{id}")
    public void getLogsById(@PathVariable("id") Long id) {
        // TODO: récupérer le log (intéraction) dont l'ID est passée en paramètre
    }

    @PutMapping("/{id}")
    public Logs updateLogs(@RequestBody Logs log, @PathVariable("id") Long id) {
        return logsService.updateLogs(log, id);
    }

    @DeleteMapping("/{id}")
    public void deleteLogsById(@PathVariable("id") Long id) {
        logsService.deleteLogs(id);
    }

}
