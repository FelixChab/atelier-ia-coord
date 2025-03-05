package com.atelier.coord.Controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
public class LogsController {

    @GetMapping
    public void getLogs() {
        // TODO: récupérer l'ensemble des logs (intéractions)
    }

    @GetMapping("/{id}")
    public void getLogsById(@RequestParam int id) {
        // TODO: récupérer le log (intéraction) dont l'ID est passée en paramètre
    }

    @DeleteMapping("/{id}")
    public void deleteLogsById(@RequestParam int id) {
        // TODO: supprimer le log (intéraction) don't l'ID est passée en paramètre
    }
}
