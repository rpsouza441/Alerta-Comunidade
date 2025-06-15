package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.FailedAlertNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetFailedAlertByIdInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetFailedAlertByIdUseCase implements GetFailedAlertByIdInputPort {
    private static final Logger logger = LoggerFactory.getLogger(GetFailedAlertByIdUseCase.class);
    private final FailedAlertRepositoryPort repository;

    public GetFailedAlertByIdUseCase(FailedAlertRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public FailedAlertNotification getFailedAlertById(Long id) {
        logger.info("Executando caso de uso: GetFailedAlertById - ID {}", id);
        return repository.findById(id)
                .orElseThrow(() -> new FailedAlertNotFoundException(id));
    }
}