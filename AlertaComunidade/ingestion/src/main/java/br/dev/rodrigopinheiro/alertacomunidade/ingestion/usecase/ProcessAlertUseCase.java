package br.dev.rodrigopinheiro.alertacomunidade.ingestion.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.common.mapper.AlertMapper;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.input.ProcessAlertInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.output.AlertPublisherPort;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.output.AlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.common.dto.AlertRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessAlertUseCase  implements ProcessAlertInputPort {
    private static final Logger logger = LoggerFactory.getLogger(ProcessAlertUseCase.class);

    private final AlertRepositoryPort repository;
    private final AlertPublisherPort publisher;

    public ProcessAlertUseCase(AlertRepositoryPort repository, AlertPublisherPort publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    @Override
    public void processAlert(AlertRequestDTO dto) {
        logger.info("Processando alerta: origem={}, tipo={}", dto.getOrigin(), dto.getAlertType());
        try {
            AlertNotification entity = AlertMapper.toEntity(dto);
            AlertNotification saved = repository.save(entity);
            publisher.publish(entity);
            logger.info("Alerta salvo e publicado com sucesso. ID: {}", saved.getId());
        } catch (Exception e) {
            logger.error("Erro ao processar alerta: {}", e.getMessage(), e);
            throw e;
        }
    }
}
