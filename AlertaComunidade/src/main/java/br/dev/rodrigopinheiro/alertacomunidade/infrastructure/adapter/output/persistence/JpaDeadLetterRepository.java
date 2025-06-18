package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.DeadLetterStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.DeadLetterRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence.jpa.SpringDataDeadLetterRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class JpaDeadLetterRepository implements DeadLetterRepositoryPort {

    private final SpringDataDeadLetterRepository delagate;

    public JpaDeadLetterRepository(SpringDataDeadLetterRepository delagate) {
        this.delagate = delagate;
    }

    @Override
    public DeadLetterMessage save(DeadLetterMessage deadLetterMessage) {
        return delagate.save(deadLetterMessage);
    }

    @Override
    public Page<DeadLetterMessage> findAllByStatus(DeadLetterStatus status, Pageable pageable) {
        return delagate.findByStatus(status, pageable);
    }

    @Override
    public Optional<DeadLetterMessage> findById(Long id) {
        return delagate.findById(id);
    }
}
