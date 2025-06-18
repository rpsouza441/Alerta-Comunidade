package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetAllQuarantinedMessagesInputPort {
    Page<DeadLetterMessage> getAll(Pageable pageable);
}
