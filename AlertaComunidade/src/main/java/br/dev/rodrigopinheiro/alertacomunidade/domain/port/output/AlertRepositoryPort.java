package br.dev.rodrigopinheiro.alertacomunidade.domain.port.output;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface AlertRepositoryPort {

    AlertNotification save(AlertNotification alert);
    Optional<AlertNotification> findById(Long id);
    Page<AlertNotification> findAll(Pageable pageable);

}
