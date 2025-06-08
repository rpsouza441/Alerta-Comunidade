package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.application.application.mapper.FailedAlertMapper;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.exception.FailedAlertNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.input.ReprocessFailedAlertUseCasePort;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.output.AlertPublisherPort;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.output.FailedAlertRepositoryPort;
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
