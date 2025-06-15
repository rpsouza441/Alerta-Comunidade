package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;

public interface ProcessFailedAlertInputPort {
    FailedAlertNotification execute(AlertNotification alert, String errorMessage);
}