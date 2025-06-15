package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;

public interface ProcessFailedAlertInputPort {
    void processFailedAlert(AlertNotification alert, String errorMessage);
}