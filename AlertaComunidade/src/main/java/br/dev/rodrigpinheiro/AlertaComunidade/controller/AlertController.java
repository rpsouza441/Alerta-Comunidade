package br.dev.rodrigpinheiro.AlertaComunidade.controller;

import br.dev.rodrigpinheiro.AlertaComunidade.dto.AlertRequestDTO;
import br.dev.rodrigpinheiro.AlertaComunidade.dto.AlertResponseDTO;
import br.dev.rodrigpinheiro.AlertaComunidade.service.AlertService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping
    public Page<AlertResponseDTO> listAlerts(
            @PageableDefault(
                        size = 10,
                        sort = "createdAt",
                        direction = Sort.Direction.DESC)
                        Pageable pageable
                        ) {
        return alertService.getAllAlerts(pageable);
    }
}
