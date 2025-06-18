package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.DeadLetterStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetAllDeadLettersInputPort {
    Page<DeadLetterMessage> getAll(DeadLetterStatus status, Pageable pageable);
}
