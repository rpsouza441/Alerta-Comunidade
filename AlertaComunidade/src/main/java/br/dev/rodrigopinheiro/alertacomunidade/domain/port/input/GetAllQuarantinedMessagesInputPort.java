package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.QuarantinedMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GetAllQuarantinedMessagesInputPort {
    Page<QuarantinedMessage> getAll(Pageable pageable);
}
