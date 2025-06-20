package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence.jpa;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SpringDataAlertRepository extends JpaRepository<AlertNotification, Long> {
}
