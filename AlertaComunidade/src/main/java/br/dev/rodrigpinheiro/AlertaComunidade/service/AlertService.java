package br.dev.rodrigpinheiro.AlertaComunidade.service;

import br.dev.rodrigpinheiro.AlertaComunidade.dto.AlertRequestDTO;
import br.dev.rodrigpinheiro.AlertaComunidade.dto.AlertResponseDTO;
import br.dev.rodrigpinheiro.AlertaComunidade.enums.AlertStatus;
import br.dev.rodrigpinheiro.AlertaComunidade.mapper.AlertMapper;
import br.dev.rodrigpinheiro.AlertaComunidade.model.AlertNotification;
import br.dev.rodrigpinheiro.AlertaComunidade.queue.AlertPublisher;
import br.dev.rodrigpinheiro.AlertaComunidade.repository.AlertNotificationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AlertService {
    private final AlertNotificationRepository repository;
    private final AlertPublisher publisher;

    public AlertService(AlertNotificationRepository repository, AlertPublisher publisher) {
        this.repository = repository;
        this.publisher = publisher;
    }

    public void processAlert(AlertRequestDTO dto){
        AlertNotification entity = AlertMapper.toEntity(dto); // uso do mapper
        repository.save(entity);
        publisher.sendToQueue(entity);
    }

    public Page<AlertResponseDTO> getAllAlerts(Pageable pageable){
        return repository.findAll(pageable).map(AlertMapper::toResponseDTO);
    }
}
