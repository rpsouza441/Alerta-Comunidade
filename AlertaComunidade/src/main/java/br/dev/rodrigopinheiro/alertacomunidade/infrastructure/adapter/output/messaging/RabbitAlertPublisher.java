package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.messaging;

import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertPublisherPort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.config.RabbitMQConfig;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class RabbitAlertPublisher implements AlertPublisherPort {

    private static final Logger logger = LoggerFactory.getLogger(RabbitAlertPublisher.class);
    private final RabbitTemplate rabbitTemplate;

    public RabbitAlertPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void publish(AlertNotification alert) {
        String routingKey = resolveRoutingKey(alert.getAlertType());
        try {
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.EXCHANGE,
                    routingKey,
                    alert
            );
            logger.info("Alerta enviado para a exchange [{}] com routingKey [{}]. ID: {}, Tipo: {}",
                    RabbitMQConfig.EXCHANGE, routingKey, alert.getId(), alert.getAlertType());
        } catch (Exception e) {
            logger.error("Erro ao enviar alerta para a fila - ID: {} - Motivo: {}", alert.getId(), e.getMessage(), e);
            throw e;
        }
    }

    private String resolveRoutingKey(AlertType alertType) {
        if (alertType == null) return RabbitMQConfig.LOG_ROUTING_KEY;

        return switch (alertType) {
            case FIRE, FLOOD, CRIME -> RabbitMQConfig.CRITICAL_ROUTING_KEY;
            case WEATHER, MEDICAL -> RabbitMQConfig.NORMAL_ROUTING_KEY;
            default -> RabbitMQConfig.LOG_ROUTING_KEY;
        };
    }
}
