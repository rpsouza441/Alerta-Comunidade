package br.dev.rodrigopinheiro.alertacomunidade.domain.port.output;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.QuarantinedMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuarantinedMessageRepositoryPort {
    QuarantinedMessage save(QuarantinedMessage quarantinedMessage);
    Page<QuarantinedMessage> findAll(Pageable pageable);
}
