package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.QuarantinedMessage;
import br.dev.rodrigopinheiro.alertacomunidade.dto.DeadLetterRequestDTO;

public interface ProcessQuarantinedMessageInputPort {
    QuarantinedMessage execute(DeadLetterRequestDTO dto);
}
