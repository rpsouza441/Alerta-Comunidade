package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.application.application.mapper.AlertMapper;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.input.GetAllAlertsInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.output.AlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.common.dto.AlertResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetAllAlertsUseCase implements GetAllAlertsInputPort {
    private static final Logger logger = LoggerFactory.getLogger(GetAllAlertsUseCase.class);
    private final AlertRepositoryPort repository;

    public GetAllAlertsUseCase(AlertRepositoryPort repository) {
        this.repository = repository;
    }
    @Override
    public Page<AlertResponseDTO> getAllAlerts(Pageable pageable) {
        logger.info("Executando caso de uso: GetAllAlerts");
        return repository.findAll(pageable).map(AlertMapper::toResponseDTO);
    }
}
