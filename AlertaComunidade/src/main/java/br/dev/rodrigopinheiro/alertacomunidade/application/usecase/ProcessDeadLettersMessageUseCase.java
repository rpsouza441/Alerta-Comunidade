package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.QuarantinedMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ProcessQuarantinedMessageInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.QuarantinedMessageRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.DeadLetterRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessDeadLettersMessageUseCase implements ProcessQuarantinedMessageInputPort {
    private static final Logger logger = LoggerFactory.getLogger(ProcessDeadLettersMessageUseCase.class);
    private final QuarantinedMessageRepositoryPort repository;

    public ProcessDeadLettersMessageUseCase(QuarantinedMessageRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public QuarantinedMessage execute(DeadLetterRequestDTO dto) {
        QuarantinedMessage message = new QuarantinedMessage();
        message.setQueueName(dto.queueName());
        message.setPayload(dto.paylod());
        message.setHeaders(dto.headers());
        QuarantinedMessage saved = repository.save(message);
        logger.debug("Quarantined message persisted. id={}", saved.getId());
        return saved;
    }

}
