package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence.jpa;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.QuarantinedMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringDataQuarantinedMessageRepository extends JpaRepository<QuarantinedMessage, Long> {
}
