package br.dev.rodrigopinheiro.alertacomunidade.domain.port.output;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.QuarantinedMessage;

public interface QuarantinedMessageRepositoryPort {
    QuarantinedMessage save(QuarantinedMessage quarantinedMessage);
}
