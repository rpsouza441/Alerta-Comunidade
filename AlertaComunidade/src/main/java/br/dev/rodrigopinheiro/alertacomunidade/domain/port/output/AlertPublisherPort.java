package br.dev.rodrigopinheiro.alertacomunidade.domain.port.output;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;

public interface AlertPublisherPort {

    void publish(AlertNotification alert);

}