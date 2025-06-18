package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.QuarantinedMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAllQuarantinedMessagesInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.QuarantinedMessageRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetAllQuarantinedMessagesUseCase implements GetAllQuarantinedMessagesInputPort {

    private static final Logger logger = LoggerFactory.getLogger(GetAllQuarantinedMessagesUseCase.class);

    private final QuarantinedMessageRepositoryPort repository;

    public GetAllQuarantinedMessagesUseCase(QuarantinedMessageRepositoryPort repository) {
        this.repository = repository;
    }


    @Override
    public Page<QuarantinedMessage> getAll(Pageable pageable) {
        logger.info("Executando caso de uso: GetAllQuarantinedMessages");
        return repository.findAll(pageable);    }
}
