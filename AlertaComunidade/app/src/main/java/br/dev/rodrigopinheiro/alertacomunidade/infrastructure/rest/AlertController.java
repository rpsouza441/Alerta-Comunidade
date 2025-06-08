package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.rest;

import br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.input.GetAlertByIdInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.input.GetAllAlertsInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.application.dto.AlertResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alerts")
public class AlertController {

    private static final Logger logger = LoggerFactory.getLogger(AlertController.class);

    private final GetAllAlertsInputPort getAllAlertsUseCase;
    private final GetAlertByIdInputPort getAlertByIdUseCase;

    public AlertController(GetAllAlertsInputPort getAllAlertsUseCase, GetAlertByIdInputPort getAlertByIdUseCase) {
        this.getAllAlertsUseCase = getAllAlertsUseCase;
        this.getAlertByIdUseCase = getAlertByIdUseCase;
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
