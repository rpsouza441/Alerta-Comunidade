package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.application.mapper.AlertMapper;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ProcessAlertInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertPublisherPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.AlertRequestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ProcessAlertUseCase  implements ProcessAlertInputPort {
    private static final Logger logger = LoggerFactory.getLogger(ProcessAlertUseCase.class);

    private final AlertPublisherPort publisher;

    public ProcessAlertUseCase(AlertPublisherPort publisher) {
        this.publisher = publisher;
    }

    @Override
    public void processAlert(AlertRequestDTO dto) {
        logger.info("Processando alerta: origem={}, tipo={}", dto.getOrigin(), dto.getAlertType());
        try {
            AlertNotification entity = AlertMapper.toEntity(dto);
            publisher.publish(entity);
            logger.info("Alerta publicado com sucesso.");
        } catch (Exception e) {
            logger.error("Erro ao processar alerta: {}", e.getMessage(), e);
            throw e;
        }
    }
}
