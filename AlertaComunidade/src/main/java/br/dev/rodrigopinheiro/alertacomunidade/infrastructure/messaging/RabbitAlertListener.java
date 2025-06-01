package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.messaging;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import static br.dev.rodrigopinheiro.alertacomunidade.infrastructure.config.RabbitMQConfig.CRITICAL_QUEUE;
import static br.dev.rodrigopinheiro.alertacomunidade.infrastructure.config.RabbitMQConfig.NORMAL_QUEUE;
import static br.dev.rodrigopinheiro.alertacomunidade.infrastructure.config.RabbitMQConfig.LOG_QUEUE;

@Component
public class RabbitAlertListener {

    private static final Logger logger = LoggerFactory.getLogger(RabbitAlertListener.class);
    private final AlertRepositoryPort repository;

    public RabbitAlertListener(AlertRepositoryPort repository) {
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
