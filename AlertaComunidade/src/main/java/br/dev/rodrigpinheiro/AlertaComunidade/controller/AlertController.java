package br.dev.rodrigpinheiro.AlertaComunidade.controller;

import br.dev.rodrigpinheiro.AlertaComunidade.dto.AlertRequestDTO;
import br.dev.rodrigpinheiro.AlertaComunidade.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public ResponseEntity<Void> receiveAlert(@Valid @RequestBody AlertRequestDTO dto) {
        alertService.processAlert(dto);
        return ResponseEntity.accepted().build();
    }
}
