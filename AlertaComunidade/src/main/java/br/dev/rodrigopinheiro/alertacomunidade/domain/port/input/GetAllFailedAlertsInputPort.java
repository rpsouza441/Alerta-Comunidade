package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;

import java.util.List;

public interface GetAllFailedAlertsInputPort {
    List<FailedAlertNotification> getAllFailedAlerts();
}