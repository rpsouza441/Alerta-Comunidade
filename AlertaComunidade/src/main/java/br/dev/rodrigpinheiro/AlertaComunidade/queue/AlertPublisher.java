package br.dev.rodrigpinheiro.AlertaComunidade.queue;

import br.dev.rodrigpinheiro.AlertaComunidade.model.AlertNotification;
import org.hibernate.annotations.Comment;
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

    // Envia o alerta para a fila, via exchange + routing key
    public void sendToQueue(AlertNotification alert) {
        rabbitTemplate.convertAndSend(
                "alerts.exchange",      // Nome da exchange (configurada em RabbitMQConfig)
                "alerts.routingkey",    // Routing key definida na binding
                alert                   // Objeto que será serializado e enviado
        );
        logger.info("Alerta enviado à fila com ID {} e tipo {}", alert.getId(), alert.getAlertType());

    }
}
