package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.input.messaging;

import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ProcessDeadLetterInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.DeadLetterRequestDTO;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class DeadLetterQueueListener {

    private static final Logger logger = LoggerFactory.getLogger(DeadLetterQueueListener.class);
    private final ProcessDeadLetterInputPort quarantineUseCase;

    public DeadLetterQueueListener(ProcessDeadLetterInputPort deadLettersInputPort) {
        this.quarantineUseCase = deadLettersInputPort;
    }

    @RabbitListener(queues = RabbitMQConfig.DEAD_CRITICAL_QUEUE)
    public void receiveDeadCritical(Message failedMessage) {
        logger.info("MENSAGEM IRRECUPERAVEL NA DQL: {}", failedMessage);
        handleMessage(RabbitMQConfig.DEAD_CRITICAL_QUEUE, failedMessage);
    }

    @RabbitListener(queues = RabbitMQConfig.DEAD_NORMAL_QUEUE)
    public void receiveDeadNormal(Message failedMessage) {
        handleMessage(RabbitMQConfig.DEAD_NORMAL_QUEUE, failedMessage);
    }

    private void handleMessage(String queue, Message failedMessage) {
        logger.error("MENSAGEM IRRECUPERÁVEL NA DLQ!");
        String messageBody = new String(failedMessage.getBody());
        var headers = failedMessage.getMessageProperties().getHeaders();
        try {
            logger.error("Payload: {}\nHeaders: {}", messageBody, headers);
            DeadLetterRequestDTO dto = new DeadLetterRequestDTO(queue, messageBody, headers.toString());
            quarantineUseCase.execute(dto);
        } catch (Throwable t) {
            logger.error("ERRO CRÍTICO no consumidor da DLQ ao tentar tratar a mensagem: {}", failedMessage.getMessageProperties().getMessageId(), t);
            logger.error("Payload: {}\nHeaders: {}", messageBody, headers);
        }

    }
}
