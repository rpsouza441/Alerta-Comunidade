package br.dev.rodrigopinheiro.alertacomunidade.domain.port.input;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GetAllFailedAlertsInputPort {
    Page<FailedAlertNotification> getAllFailedAlerts(Pageable pageable);
}