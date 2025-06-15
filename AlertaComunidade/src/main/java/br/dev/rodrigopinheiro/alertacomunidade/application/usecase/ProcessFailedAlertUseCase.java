package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.FailedAlertMapper;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ProcessFailedAlertInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessFailedAlertUseCase implements ProcessFailedAlertInputPort {
    private static final Logger logger = LoggerFactory.getLogger(ProcessFailedAlertUseCase.class);
    private final FailedAlertRepositoryPort repository;

    public ProcessFailedAlertUseCase(FailedAlertRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public void processFailedAlert(AlertNotification alert, String errorMessage) {
        logger.info("Processando alerta falho: id={}, motivo={}", alert.getId(), errorMessage);
        repository.save(FailedAlertMapper.from(alert, errorMessage));
    }
}