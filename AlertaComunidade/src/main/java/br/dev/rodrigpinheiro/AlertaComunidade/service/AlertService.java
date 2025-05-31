package br.dev.rodrigpinheiro.AlertaComunidade.service;

import br.dev.rodrigpinheiro.AlertaComunidade.controller.AlertController;
import br.dev.rodrigpinheiro.AlertaComunidade.dto.AlertRequestDTO;
import br.dev.rodrigpinheiro.AlertaComunidade.dto.AlertResponseDTO;
import br.dev.rodrigpinheiro.AlertaComunidade.enums.AlertStatus;
import br.dev.rodrigpinheiro.AlertaComunidade.exception.ResourceNotFoundException;
import br.dev.rodrigpinheiro.AlertaComunidade.mapper.AlertMapper;
import br.dev.rodrigpinheiro.AlertaComunidade.model.AlertNotification;
import br.dev.rodrigpinheiro.AlertaComunidade.queue.AlertPublisher;
import br.dev.rodrigpinheiro.AlertaComunidade.repository.AlertNotificationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlertService {
    private final AlertNotificationRepository repository;
    private final AlertPublisher publisher;
    private static final Logger logger = LoggerFactory.getLogger(AlertService.class);

    public AlertService(AlertNotificationRepository repository, AlertPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    public void processAlert(AlertRequestDTO dto) {
        logger.info("Processando alerta com origem: {}", dto.getOrigin());
        try {
            AlertNotification entity = AlertMapper.toEntity(dto);
            repository.save(entity);
            publisher.sendToQueue(entity);
        } catch (Exception e) {
            logger.error("Erro ao processar alerta: {}", e.getMessage(), e);
            throw e;
        }
    }

    public Page<AlertResponseDTO> getAllAlerts(Pageable pageable) {
        return repository.findAll(pageable).map(AlertMapper::toResponseDTO);
    }

    public AlertResponseDTO getAlertById(Long id) {
        AlertNotification entity = repository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Alerta com ID: " + id + " não encontrada"));

        return AlertMapper.toResponseDTO(entity);
    }
}
