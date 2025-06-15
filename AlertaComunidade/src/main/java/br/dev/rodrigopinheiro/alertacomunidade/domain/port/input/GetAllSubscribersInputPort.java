package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberResponseDTO;

import java.util.List;

public interface GetAllSubscribersInputPort {
    List<SubscriberResponseDTO> getAll();
}