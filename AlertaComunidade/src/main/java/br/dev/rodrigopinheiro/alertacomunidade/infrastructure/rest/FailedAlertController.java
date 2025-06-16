package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.rest;

import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.FailedAlertNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAllFailedAlertsInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ReprocessFailedAlertUseCasePort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/failed-alerts")
public class FailedAlertController {

    private final ReprocessFailedAlertUseCasePort reprocessFailedAlertUseCasePort;
    private final GetAllFailedAlertsInputPort getAllFailedAlertsUseCase;


    public FailedAlertController(
            ReprocessFailedAlertUseCasePort reprocessFailedAlertUseCasePort,
            GetAllFailedAlertsInputPort getAllFailedAlertsUseCase) {
        this.getAllFailedAlertsUseCase = getAllFailedAlertsUseCase;
        this.reprocessFailedAlertUseCasePort = reprocessFailedAlertUseCasePort;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<FailedAlertNotification> listAll(
            @PageableDefault(size = 10, sort = "failedAt", direction = Sort.Direction.DESC)
            Pageable pageable) {
        return getAllFailedAlertsUseCase.getAllFailedAlerts(pageable);
    }

    @PostMapping("/{id}/reprocess")
    public ResponseEntity<String> reprocess(@PathVariable Long id) {
        try {
            reprocessFailedAlertUseCasePort.execute(id);
            return ResponseEntity.ok("Alerta reenviado com sucesso para a fila");
        } catch (FailedAlertNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro ao reprocessar alerta");
        }

    }


}
