package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.input.messaging;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.AlertProcessingException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ProcessFailedAlertInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.*;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.notification.SubscriberNotifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import static br.dev.rodrigopinheiro.alertacomunidade.infrastructure.config.RabbitMQConfig.CRITICAL_QUEUE;
import static br.dev.rodrigopinheiro.alertacomunidade.infrastructure.config.RabbitMQConfig.NORMAL_QUEUE;
import static br.dev.rodrigopinheiro.alertacomunidade.infrastructure.config.RabbitMQConfig.LOG_QUEUE;

@Component
public class RabbitAlertListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitAlertListener.class);
    private final AlertRepositoryPort alertRepository;
    private final ProcessFailedAlertInputPort processFailedAlertUseCasePort;
    private final SubscriberNotifier subscriberNotifier;
    private final BackupStoragePort backupStoragePort;

    public RabbitAlertListener(AlertRepositoryPort repository,
                               ProcessFailedAlertInputPort processFailedAlertUseCasePort,
                               SubscriberNotifier subscriberNotifier,
                               BackupStoragePort backupStoragePort) {
        this.alertRepository = repository;
        this.processFailedAlertUseCasePort = processFailedAlertUseCasePort;
        this.subscriberNotifier = subscriberNotifier;
        this.backupStoragePort = backupStoragePort;
    }

    @Retryable(
            retryFor = {AlertProcessingException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @RabbitListener(queues = CRITICAL_QUEUE)
    public void receiveCritical(@Payload AlertNotification alert) {
        logger.info("[CRITICAL] Alerta recebido: {} - Tipo: {}", alert.getMessage(), alert.getAlertType());
        try {
            alert.setStatus(AlertStatus.DELIVERED);
            alertRepository.save(alert);
            subscriberNotifier.notifySubscribers(alert);

        } catch (AlertProcessingException e) {
            logger.error("Falha ao processar alerta: ID={} - Motivo: {}", alert.getId(), e.getMessage(), e);
            throw new AlertProcessingException("Erro ao processar alerta", e);
        }
    }

    @Retryable(
            retryFor = {AlertProcessingException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 2000, multiplier = 2)
    )
    @RabbitListener(queues = NORMAL_QUEUE)
    public void receiveNormal(@Payload AlertNotification alert) {
        logger.info("[Normal] Alerta recebido: {} - Tipo: {}", alert.getMessage(), alert.getAlertType());
        try {
            alert.setStatus(AlertStatus.DELIVERED);
            alertRepository.save(alert);
            subscriberNotifier.notifySubscribers(alert);

        } catch (AlertProcessingException e) {
            logger.error("Falha ao processar alerta: ID={} - Motivo: {}", alert.getId(), e.getMessage(), e);
            throw new AlertProcessingException("Erro ao processar alerta", e);
        }
    }

    @Retryable(
            retryFor = {AlertProcessingException.class},
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
        logger.error("RECUPERAÇÃO: Todas as tentativas falharam para o alerta. Movendo para a fila de Parking Lot.", e);
        try {
            processFailedAlertUseCasePort.execute(alert, e.getMessage());
        } catch (Exception pubEx) {
            logger.error("ERRO CRÍTICO: Falha ao mover alerta ID {} para a fila de Parking Lot.", alert.getId(), pubEx);
            backupStoragePort.save(e.getMessage(), alert);
        }
    }


}
