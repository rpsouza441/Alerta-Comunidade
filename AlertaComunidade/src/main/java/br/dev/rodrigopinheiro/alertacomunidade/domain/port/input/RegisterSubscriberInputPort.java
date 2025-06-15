package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;


import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberRequestDTO;

public interface RegisterSubscriberInputPort {
    Subscriber register(SubscriberRequestDTO dto);
}