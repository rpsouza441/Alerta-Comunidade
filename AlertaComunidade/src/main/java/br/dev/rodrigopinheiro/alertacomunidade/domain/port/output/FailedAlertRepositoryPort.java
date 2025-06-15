package br.dev.rodrigopinheiro.alertacomunidade.domain.port.output;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface FailedAlertRepositoryPort {
    FailedAlertNotification save(FailedAlertNotification failed);

    List<FailedAlertNotification> findAll();

    Page<FailedAlertNotification> findAll(Pageable pageable);

    Optional<FailedAlertNotification> findById(Long id);

}
