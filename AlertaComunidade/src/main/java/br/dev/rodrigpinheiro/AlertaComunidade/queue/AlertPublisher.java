package br.dev.rodrigpinheiro.AlertaComunidade.queue;

import br.dev.rodrigpinheiro.AlertaComunidade.config.RabbitMQConfig;
import br.dev.rodrigpinheiro.AlertaComunidade.enums.AlertType;
import br.dev.rodrigpinheiro.AlertaComunidade.model.AlertNotification;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class AlertPublisher {

    private static final Logger logger = LoggerFactory.getLogger(AlertPublisher.class);

    private final RabbitTemplate rabbitTemplate;

    public AlertPublisher(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToQueue(AlertNotification alert) {
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

        switch (alertType) {
            case FIRE:
            case FLOOD:
            case CRIME:
                return RabbitMQConfig.CRITICAL_ROUTING_KEY;

            case WEATHER:
            case MEDICAL:
                return RabbitMQConfig.NORMAL_ROUTING_KEY;

            default:
                return RabbitMQConfig.LOG_ROUTING_KEY;
        }
    }
}
