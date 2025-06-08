package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.AlertMapper;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.ResourceNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAlertByIdInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.common.dto.AlertResponseDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class GetAlertByIdUseCase implements GetAlertByIdInputPort {
    private static final Logger logger = LoggerFactory.getLogger(GetAlertByIdUseCase.class);
    private final AlertRepositoryPort repository;

    public GetAlertByIdUseCase(AlertRepositoryPort repository) {
        this.repository = repository;
    }

    @Override
    public AlertResponseDTO getAlertById(Long id) {
        logger.info("Executando caso de uso: GetAlertById - ID {}", id);
        AlertNotification entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Alerta com ID: " + id + " não encontrada"));

        return AlertMapper.toResponseDTO(entity);
    }

}
