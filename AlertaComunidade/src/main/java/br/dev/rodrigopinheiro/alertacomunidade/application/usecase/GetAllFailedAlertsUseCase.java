package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;


import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAllFailedAlertsInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


@Service
public class GetAllFailedAlertsUseCase implements GetAllFailedAlertsInputPort {
    private static final Logger logger = LoggerFactory.getLogger(GetAllFailedAlertsUseCase.class);
    private final FailedAlertRepositoryPort repository;

    public GetAllFailedAlertsUseCase(FailedAlertRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public Page<FailedAlertNotification> getAllFailedAlerts(Pageable pageable) {
        logger.info("Executando caso de uso: GetAllFailedAlerts");
        return repository.findAll(Pageable pageable);
    }
}