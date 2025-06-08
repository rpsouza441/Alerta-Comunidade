package br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.output;

import br.dev.rodrigopinheiro.alertacomunidade.application.domain.model.AlertNotification;

public interface AlertPublisherPort {

    void publish(AlertNotification alert);

}