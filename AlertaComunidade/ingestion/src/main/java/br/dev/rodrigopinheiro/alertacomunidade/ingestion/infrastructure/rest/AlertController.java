package br.dev.rodrigopinheiro.alertacomunidade.ingestion.infrastructure.rest;

import br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.input.GetAlertByIdInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.input.GetAllAlertsInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.input.ProcessAlertInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.common.dto.AlertRequestDTO;
import br.dev.rodrigopinheiro.alertacomunidade.common.dto.AlertResponseDTO;
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

    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);

    private final ProcessAlertInputPort processAlertUseCase;
    private final GetAllAlertsInputPort getAllAlertsUseCase;
    private final GetAlertByIdInputPort getAlertByIdUseCase;

    public AlertController(ProcessAlertInputPort processAlertUseCase,
                           GetAllAlertsInputPort getAllAlertsUseCase,
                           GetAlertByIdInputPort getAlertByIdUseCase) {
        this.processAlertUseCase = processAlertUseCase;
        this.getAllAlertsUseCase = getAllAlertsUseCase;
        this.getAlertByIdUseCase = getAlertByIdUseCase;
    }


    @PostMapping
    public ResponseEntity<?> receiveAlert(@Valid @RequestBody AlertRequestDTO dto) {
        logger.info("POST /alerts - origin={}, type={}", dto.getOrigin(), dto.getAlertType());
        processAlertUseCase.processAlert(dto);
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
        return getAllAlertsUseCase.getAllAlerts(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AlertResponseDTO> getAlertbyId(@PathVariable Long id) {
        logger.info("GET /alerts - ID={}", id);
        AlertResponseDTO alertResponseDTO = getAlertByIdUseCase.getAlertById(id);
        return  ResponseEntity.ok(alertResponseDTO);
    }
}