package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.DeadLetterMessageRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence.jpa.SpringDataDeadLetterMessageRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class JpaDeadLetterMessageRepository implements DeadLetterMessageRepositoryPort {

    private final SpringDataDeadLetterMessageRepository delagate;

    public JpaDeadLetterMessageRepository(SpringDataDeadLetterMessageRepository delagate) {
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
