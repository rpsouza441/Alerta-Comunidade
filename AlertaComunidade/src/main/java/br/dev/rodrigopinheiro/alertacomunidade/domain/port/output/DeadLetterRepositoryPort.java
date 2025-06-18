package br.dev.rodrigopinheiro.alertacomunidade.domain.port.output;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.DeadLetterStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface DeadLetterRepositoryPort {
    DeadLetterMessage save(DeadLetterMessage deadLetterMessage);
    Page<DeadLetterMessage> findAllByStatus(DeadLetterStatus status, Pageable pageable);
    Optional<DeadLetterMessage> findById(Long id);
}
