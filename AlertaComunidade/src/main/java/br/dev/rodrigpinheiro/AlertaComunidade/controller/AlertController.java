package br.dev.rodrigpinheiro.AlertaComunidade.controller;

import br.dev.rodrigpinheiro.AlertaComunidade.dto.AlertRequestDTO;
import br.dev.rodrigpinheiro.AlertaComunidade.dto.AlertResponseDTO;
import br.dev.rodrigpinheiro.AlertaComunidade.service.AlertService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private final AlertService alertService;
    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);

    public AlertController(AlertService alertService) {
        this.alertService = alertService;
    }

    @PostMapping
    public ResponseEntity<?> receiveAlert(@Valid @RequestBody AlertRequestDTO dto) {
        logger.info("POST /alerts - origin={}, type={}", dto.getOrigin(), dto.getAlertType());
        alertService.processAlert(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public Page<AlertResponseDTO> listAlerts(
            @PageableDefault(
                        size = 10,
                        sort = "createdAt",
                        direction = Sort.Direction.DESC)
                        Pageable pageable
                        ) {
        logger.info("GET /alerts");


        return alertService.getAllAlerts(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertResponseDTO> getAlertbyId(@PathVariable Long id) {
        logger.info("GET /alerts - ID={}", id);

        AlertResponseDTO alertResponseDTO = alertService.getAlertById(id);
        return  ResponseEntity.ok(alertResponseDTO);
    }
}
