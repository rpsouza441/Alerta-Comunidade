package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface GetAllSubscribersInputPort {
    Page<SubscriberResponseDTO> getAll(Pageable pageable);
}