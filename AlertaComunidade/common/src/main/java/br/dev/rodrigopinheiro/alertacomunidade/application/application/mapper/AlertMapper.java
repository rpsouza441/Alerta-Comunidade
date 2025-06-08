package br.dev.rodrigopinheiro.alertacomunidade.application.application.mapper;


import br.dev.rodrigopinheiro.alertacomunidade.application.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.application.dto.AlertRequestDTO;
import br.dev.rodrigopinheiro.alertacomunidade.application.dto.AlertResponseDTO;

public class AlertMapper {


    public static AlertNotification toEntity(AlertRequestDTO dto) {
        AlertNotification entity = new AlertNotification();
        entity.setMessage(dto.getMessage());
        entity.setOrigin(dto.getOrigin());
        entity.setAlertType(dto.getAlertType());
        entity.setStatus(AlertStatus.RECEIVED);
        return entity;
    }

    public static AlertResponseDTO toResponseDTO(AlertNotification alertNotification) {
        return new AlertResponseDTO(
                alertNotification.getId(),
                alertNotification.getMessage(),
                alertNotification.getOrigin(),
                alertNotification.getAlertType(),
                alertNotification.getStatus(),
                alertNotification.getCreatedAt()
        );
    }
}
