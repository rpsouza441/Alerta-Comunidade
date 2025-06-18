package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.QuarantinedMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.QuarantinedMessageRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence.jpa.SpringDataQuarantinedMessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Page<QuarantinedMessage> findAll(Pageable pageable) {
        return delagate.findAll(pageable);
    }
}
