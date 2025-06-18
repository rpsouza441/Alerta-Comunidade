package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.QuarantinedMessage;
import br.dev.rodrigopinheiro.alertacomunidade.dto.QuarantinedMessageRequestDTO;

public interface ProcessQuarantinedMessageInputPort {
    QuarantinedMessage execute(QuarantinedMessageRequestDTO dto);
}
