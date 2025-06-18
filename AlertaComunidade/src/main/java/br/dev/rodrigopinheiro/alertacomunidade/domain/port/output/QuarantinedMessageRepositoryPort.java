package br.dev.rodrigopinheiro.alertacomunidade.domain.port.output;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuarantinedMessageRepositoryPort {
    DeadLetterMessage save(DeadLetterMessage quarantinedMessage);
    Page<DeadLetterMessage> findAll(Pageable pageable);
}
