package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberResponseDTO;

public interface GetSubscriberByIdInputPort {
    SubscriberResponseDTO getById(Long id);
}