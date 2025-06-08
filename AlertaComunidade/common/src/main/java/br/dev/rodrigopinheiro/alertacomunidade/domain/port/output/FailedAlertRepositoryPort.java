package br.dev.rodrigopinheiro.alertacomunidade.domain.port.output;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;

import java.util.List;
import java.util.Optional;

public interface FailedAlertRepositoryPort {
    FailedAlertNotification save(FailedAlertNotification failed);

    List<FailedAlertNotification> findAll();

    Optional<FailedAlertNotification> findById(Long id);

}
