package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;

public interface GetFailedAlertByIdInputPort {
    FailedAlertNotification getFailedAlertById(Long id);
}