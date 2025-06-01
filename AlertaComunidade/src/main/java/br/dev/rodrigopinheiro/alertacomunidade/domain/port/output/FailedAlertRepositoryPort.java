package br.dev.rodrigopinheiro.alertacomunidade.domain.port.output;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;

public interface FailedAlertRepositoryPort {
    FailedAlertNotification save(FailedAlertNotification failed);

}
