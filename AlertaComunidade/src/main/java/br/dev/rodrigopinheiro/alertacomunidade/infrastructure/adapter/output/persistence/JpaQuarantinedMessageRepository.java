package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
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
    public DeadLetterMessage save(DeadLetterMessage deadLetterMessage) {
        return delagate.save(deadLetterMessage);
    }

    @Override
    public Page<DeadLetterMessage> findAll(Pageable pageable) {
        return delagate.findAll(pageable);
    }
}
