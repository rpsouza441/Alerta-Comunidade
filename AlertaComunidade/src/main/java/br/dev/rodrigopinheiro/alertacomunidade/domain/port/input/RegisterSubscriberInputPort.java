package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;

public interface RegisterSubscriberInputPort {
    Subscriber register(Subscriber subscriber);
}
