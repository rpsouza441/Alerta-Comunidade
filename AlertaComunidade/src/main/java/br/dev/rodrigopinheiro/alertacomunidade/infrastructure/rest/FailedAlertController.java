package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.rest;

import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.FailedAlertMapper;
import br.dev.rodrigopinheiro.alertacomunidade.application.usecase.ReprocessFailedAlertUseCase;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.FailedAlertNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ReprocessFailedAlertUseCasePort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertPublisherPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/failed-alerts")
public class FailedAlertController {

    private final FailedAlertRepositoryPort failedAlertRepository;
    private final ReprocessFailedAlertUseCasePort reprocessFailedAlertUseCasePort;


    public FailedAlertController(FailedAlertRepositoryPort failedAlertRepository,
                                  ReprocessFailedAlertUseCasePort reprocessFailedAlertUseCasePort) {
        this.failedAlertRepository = failedAlertRepository;
        this.reprocessFailedAlertUseCasePort = reprocessFailedAlertUseCasePort;
    }

    @GetMapping
    public ResponseEntity<List<FailedAlertNotification>> listAill(){
        List<FailedAlertNotification> all = failedAlertRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @PostMapping("/{id}/reprocess")
    public ResponseEntity<String> reprocess(@PathVariable Long id){
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
