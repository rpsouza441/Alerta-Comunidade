package br.dev.rodrigopinheiro.alertacomunidade.ingestion.infrastructure.messaging;

import br.dev.rodrigopinheiro.alertacomunidade.common.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.output.AlertPublisherPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import static br.dev.rodrigopinheiro.alertacomunidade.common.config.RabbitConstants.*;

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
                    EXCHANGE,
                    routingKey,
                    alert
            );
            logger.info("Alerta enviado para a exchange [{}] com routingKey [{}]. ID: {}, Tipo: {}",
                    EXCHANGE, routingKey, alert.getId(), alert.getAlertType());
        } catch (Exception e) {
            logger.error("Erro ao enviar alerta para a fila - ID: {} - Motivo: {}", alert.getId(), e.getMessage(), e);
            throw e;
        }
    }

    private String resolveRoutingKey(AlertType alertType) {
        if (alertType == null) return LOG_ROUTING_KEY;

        return switch (alertType) {
            case FIRE, FLOOD, CRIME -> CRITICAL_ROUTING_KEY;
            case WEATHER, MEDICAL -> NORMAL_ROUTING_KEY;
            default -> LOG_ROUTING_KEY;
        };
    }
}
