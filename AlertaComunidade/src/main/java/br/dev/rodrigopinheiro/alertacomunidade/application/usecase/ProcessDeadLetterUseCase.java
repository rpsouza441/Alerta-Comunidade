package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ProcessDeadLetterInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.DeadLetterRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.DeadLetterRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessDeadLetterUseCase implements ProcessDeadLetterInputPort {
    private static final Logger logger = LoggerFactory.getLogger(ProcessDeadLetterUseCase.class);
    private final DeadLetterRepositoryPort repository;

    public ProcessDeadLetterUseCase(DeadLetterRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public DeadLetterMessage execute(DeadLetterRequestDTO dto) {
        DeadLetterMessage message = new DeadLetterMessage();
        message.setQueueName(dto.queueName());
        message.setPayload(dto.paylod());
        message.setHeaders(dto.headers());
        DeadLetterMessage saved = repository.save(message);
        logger.debug("Dead letter message persisted. id={}", saved.getId());
        return saved;
    }

}
