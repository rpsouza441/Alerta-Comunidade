package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence.jpa;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataFailedAlertRepository extends JpaRepository<FailedAlertNotification, Long> {
}
