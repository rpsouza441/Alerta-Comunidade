package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence.jpa;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataDeadLetterMessageRepository extends JpaRepository<DeadLetterMessage, Long> {
}
