package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.messaging;

import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterQueueListener {

    private static final Logger logger = LoggerFactory.getLogger(DeadLetterQueueListener.class);

    @RabbitListener(queues = RabbitMQConfig.DEAD_CRITICAL_QUEUE)
    public void receiveDeadCritical(Message failedMessage) {
        logger.info("MENSAGEM IRRECUPERAVEL NA DQL: {}", failedMessage);

        try {
            String messageBody = new String(failedMessage.getBody());
            var headers= failedMessage.getMessageProperties().getHeaders();
            logger.error(
                    "Payload {}\nHeraders: {}",
                    messageBody,
                    headers
            );
        }catch(Throwable t){
          logger.error("ERRO CRITICO no consumidor da DLQ ao tentar tratar a mensagem: {}", failedMessage.getMessageProperties().getMessageId(), t) ;
        }
    }
}
