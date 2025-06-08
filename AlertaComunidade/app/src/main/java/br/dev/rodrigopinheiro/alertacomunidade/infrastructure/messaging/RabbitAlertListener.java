package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.messaging;

import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.FailedAlertMapper;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.AlertProcessingException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import static br.dev.rodrigopinheiro.alertacomunidade.config.RabbitConstants.*;


@Component
public class RabbitAlertListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitAlertListener.class);
    private final AlertRepositoryPort alertRepository;
    private final FailedAlertRepositoryPort failedRepository;

    public RabbitAlertListener(AlertRepositoryPort repository, FailedAlertRepositoryPort failedRepository) {
        this.alertRepository = repository;
        this.failedRepository = failedRepository;
    }

    @Retryable(
            retryFor = { AlertProcessingException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @RabbitListener(queues = CRITICAL_QUEUE)
    public void receiveCritical(@Payload AlertNotification alert) {
        logger.info("[CRITICAL] Alerta recebido: {} - Tipo: {}", alert.getMessage(), alert.getAlertType());
        try {
            alert.setStatus(AlertStatus.DELIVERED);
            alertRepository.save(alert);
        } catch (AlertProcessingException e) {
            logger.error("Falha ao processar alerta: ID={} - Motivo: {}", alert.getId(), e.getMessage(), e);
            throw new AlertProcessingException("Erro ao processar alerta", e);
        }
    }

    @Retryable(
            retryFor = { AlertProcessingException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @RabbitListener(queues = NORMAL_QUEUE)
    public void receiveNormal(@Payload AlertNotification alert) {
        logger.info("[Normal] Alerta recebido: {} - Tipo: {}", alert.getMessage(), alert.getAlertType());
        try {
            alert.setStatus(AlertStatus.DELIVERED);
            alertRepository.save(alert);
        } catch (AlertProcessingException e) {
            logger.error("Falha ao processar alerta: ID={} - Motivo: {}", alert.getId(), e.getMessage(), e);
            throw new AlertProcessingException("Erro ao processar alerta", e);
        }
    }
    @Retryable(
            retryFor = { AlertProcessingException.class },
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @RabbitListener(queues = LOG_QUEUE)
    public void receiveLog(@Payload AlertNotification alert) {
        logger.info("[LOG] Alerta registrado para fins internos: {}", alert.getMessage());
        try {
            alert.setStatus(AlertStatus.DELIVERED);
            alertRepository.save(alert);
        } catch (AlertProcessingException e) {
            logger.error("Falha ao processar alerta: ID={} - Motivo: {}", alert.getId(), e.getMessage(), e);
            throw new AlertProcessingException("Erro ao processar alerta", e);
        }
    }

    @Recover
    public void recover(AlertProcessingException e, AlertNotification alert) {
        logger.warn("[RECOVER] Alerta ID={} movido para fallback após 3 tentativas. Motivo final: {}",
                alert.getId(), e.getMessage());
        FailedAlertNotification failed = FailedAlertMapper.from(alert, e.getMessage());
        failedRepository.save(failed);

        logger.info("Alerta ID={} persistido com status de falha em 'failed_alert_notifications'", alert.getId());
    }
}
