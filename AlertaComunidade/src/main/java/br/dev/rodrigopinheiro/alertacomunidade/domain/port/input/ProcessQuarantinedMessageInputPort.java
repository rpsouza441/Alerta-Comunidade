package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import br.dev.rodrigopinheiro.alertacomunidade.dto.DeadLetterRequestDTO;

public interface ProcessQuarantinedMessageInputPort {
    DeadLetterMessage execute(DeadLetterRequestDTO dto);
}
