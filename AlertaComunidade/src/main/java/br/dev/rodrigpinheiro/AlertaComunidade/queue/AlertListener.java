package br.dev.rodrigpinheiro.AlertaComunidade.queue;

import br.dev.rodrigpinheiro.AlertaComunidade.enums.AlertStatus;
import br.dev.rodrigpinheiro.AlertaComunidade.model.AlertNotification;
import br.dev.rodrigpinheiro.AlertaComunidade.repository.AlertNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static br.dev.rodrigpinheiro.AlertaComunidade.config.RabbitMQConfig.*;

@Component
public class AlertListener {

    private static final Logger logger = LoggerFactory.getLogger(AlertListener.class);

    private final AlertNotificationRepository repository;

    public AlertListener(AlertNotificationRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = CRITICAL_QUEUE)
    public void receiveCritical(@Payload AlertNotification alert) {
        logger.info("[CRITICAL] Alerta recebido: {} - Tipo: {}", alert.getMessage(), alert.getAlertType());
        try {
            alert.setStatus(AlertStatus.DELIVERED);
            repository.save(alert);
        } catch (Exception e) {
            logger.error("Falha ao processar alerta: ID={} - Motivo: {}", alert.getId(), e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    @RabbitListener(queues = NORMAL_QUEUE)
    public void receiveNormal(@Payload AlertNotification alert) {
        logger.info("[Normal] Alerta recebido: {} - Tipo: {}", alert.getMessage(), alert.getAlertType());
        try {
            alert.setStatus(AlertStatus.DELIVERED);
            repository.save(alert);
        } catch (Exception e) {
            logger.error("Falha ao processar alerta: ID={} - Motivo: {}", alert.getId(), e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
    @RabbitListener(queues = LOG_QUEUE)
    public void receiveLog(@Payload AlertNotification alert) {
        logger.info("[LOG] Alerta registrado para fins internos: {}", alert.getMessage());
        try {
            alert.setStatus(AlertStatus.DELIVERED);
            repository.save(alert);
        } catch (Exception e) {
            logger.error("Falha ao processar alerta: ID={} - Motivo: {}", alert.getId(), e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }
}
