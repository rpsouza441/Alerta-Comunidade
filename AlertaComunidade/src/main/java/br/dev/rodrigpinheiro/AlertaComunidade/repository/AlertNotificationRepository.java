package br.dev.rodrigpinheiro.AlertaComunidade.repository;

import br.dev.rodrigpinheiro.AlertaComunidade.model.AlertNotification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlertNotificationRepository extends JpaRepository<AlertNotification, Long> {
}
