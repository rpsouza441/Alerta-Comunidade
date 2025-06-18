package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.adapter;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.QuarantinedMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.QuarantinedMessageRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.jpa.SpringDataQuarantinedMessageRepository;
import org.springframework.stereotype.Component;

@Component
public class JpaQuarantinedMessageRepository implements QuarantinedMessageRepositoryPort {

    private final SpringDataQuarantinedMessageRepository delagate;

    public JpaQuarantinedMessageRepository(SpringDataQuarantinedMessageRepository delagate) {
        this.delagate = delagate;
    }

    @Override
    public QuarantinedMessage save(QuarantinedMessage quarantinedMessage) {
        return delagate.save(quarantinedMessage);
    }
}
