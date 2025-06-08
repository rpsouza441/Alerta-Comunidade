package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.infrastructure.rest;

import br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.input.ProcessAlertInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.application.dto.AlertRequestDTO;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);

    private final ProcessAlertInputPort processAlertUseCase;

    public AlertController(ProcessAlertInputPort processAlertUseCase) {
        this.processAlertUseCase = processAlertUseCase;
    }


    @PostMapping
    public ResponseEntity<?> receiveAlert(@Valid @RequestBody AlertRequestDTO dto) {
        logger.info("POST /alerts - origin={}, type={}", dto.getOrigin(), dto.getAlertType());
        processAlertUseCase.processAlert(dto);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }


}
