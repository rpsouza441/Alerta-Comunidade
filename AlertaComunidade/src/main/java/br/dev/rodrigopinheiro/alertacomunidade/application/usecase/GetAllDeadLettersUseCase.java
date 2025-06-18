package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.DeadLetterStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAllDeadLettersInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.DeadLetterRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetAllDeadLettersUseCase implements GetAllDeadLettersInputPort {

    private static final Logger logger = LoggerFactory.getLogger(GetAllDeadLettersUseCase.class);

    private final DeadLetterRepositoryPort repository;

    public GetAllDeadLettersUseCase(DeadLetterRepositoryPort repository) {
        this.repository = repository;
    }


    @Override
    public Page<DeadLetterMessage> getAll(DeadLetterStatus status, Pageable pageable) {
        logger.info("Executando caso de uso: GetAllQuarantinedMessages");
        return repository.findAllByStatus(status, pageable);    }
}
