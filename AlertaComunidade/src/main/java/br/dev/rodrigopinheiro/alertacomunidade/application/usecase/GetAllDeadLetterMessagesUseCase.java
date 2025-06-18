package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAllQuarantinedMessagesInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.DeadLetterMessageRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetAllDeadLetterMessagesUseCase implements GetAllQuarantinedMessagesInputPort {

    private static final Logger logger = LoggerFactory.getLogger(GetAllDeadLetterMessagesUseCase.class);

    private final DeadLetterMessageRepositoryPort repository;

    public GetAllDeadLetterMessagesUseCase(DeadLetterMessageRepositoryPort repository) {
        this.repository = repository;
    }


    @Override
    public Page<DeadLetterMessage> getAll(Pageable pageable) {
        logger.info("Executando caso de uso: GetAllQuarantinedMessages");
        return repository.findAll(pageable);    }
}
