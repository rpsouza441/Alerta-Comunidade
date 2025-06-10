package br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.output;

import br.dev.rodrigopinheiro.alertacomunidade.common.domain.model.AlertNotification;

public interface AlertPublisherPort {

    void publish(AlertNotification alert);

}