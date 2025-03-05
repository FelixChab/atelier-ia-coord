package com.coordia.atelier.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/corrections")
public class CorrectionController {

    @PostMapping()
    public void submitText() {
        // TODO: POST - envoi du texte à corriger
    }

}
