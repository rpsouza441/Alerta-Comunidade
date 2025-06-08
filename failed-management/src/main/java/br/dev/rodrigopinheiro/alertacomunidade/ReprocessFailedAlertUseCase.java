package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.FailedAlertMapper;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.FailedAlertNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ReprocessFailedAlertUseCasePort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertPublisherPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ReprocessFailedAlertUseCase implements ReprocessFailedAlertUseCasePort {
    private final FailedAlertRepositoryPort failedRepository;
    private final AlertPublisherPort alertPublisher;

    public ReprocessFailedAlertUseCase(FailedAlertRepositoryPort failedRepository,
                                       AlertPublisherPort alertPublisher) {
        this.failedRepository = failedRepository;
        this.alertPublisher = alertPublisher;
    }

    @Override
    public void execute(Long id) {
        FailedAlertNotification failed = failedRepository.findById(id)
                .orElseThrow(() -> new FailedAlertNotFoundException(id));

        AlertNotification alert = FailedAlertMapper.toAlertNotification(failed);
        alertPublisher.publish(alert);

        failed.setStatus(AlertStatus.REPROCESSED);
        failed.setReprocessedAt(LocalDateTime.now());
        failedRepository.save(failed);
    }
}
