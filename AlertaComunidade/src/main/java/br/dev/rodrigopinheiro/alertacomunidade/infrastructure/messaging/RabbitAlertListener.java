package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.messaging;

import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.FailedAlertMapper;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.AlertProcessingException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ProcessFailedAlertInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.NotificationServicePort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.SubscriberRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.notification.SubscriberNotifier;
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

    public RabbitAlertListener(AlertRepositoryPort repository,
                           ProcessFailedAlertInputPort processFailedAlertUseCasePort,
                               SubscriberNotifier subscriberNotifier) {
        this.alertRepository = repository;
        this.processFailedAlertUseCasePort = processFailedAlertUseCasePort;
        this.subscriberNotifier = subscriberNotifier;
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
            subscriberNotifier.notifySubscribers(alert);

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
            subscriberNotifier.notifySubscribers(alert);

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
        processFailedAlertUseCasePort.execute(alert, e.getMessage());
    }


}
