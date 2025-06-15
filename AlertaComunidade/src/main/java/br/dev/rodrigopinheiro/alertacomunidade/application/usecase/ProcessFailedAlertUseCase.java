package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.FailedAlertMapper;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ProcessFailedAlertInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessFailedAlertUseCase implements ProcessFailedAlertInputPort {
    private static final Logger logger = LoggerFactory.getLogger(ProcessFailedAlertUseCase.class);
    private final FailedAlertRepositoryPort repository;

    public ProcessFailedAlertUseCase(FailedAlertRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public FailedAlertNotification execute(AlertNotification alert, String errorMessage) {
        logger.warn("[RECOVER] Alerta ID={} movido para fallback após 3 tentativas. Motivo final: {}",
                alert.getId(), errorMessage);
        FailedAlertNotification failed = FailedAlertMapper.from(alert, errorMessage);
        repository.save(failed);
        logger.info("Alerta ID={} persistido com status de falha em 'failed_alert_notifications'", alert.getId());
        return failed;
    }
}