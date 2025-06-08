package br.dev.rodrigopinheiro.alertacomunidade.application.mapper;


import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.dto.AlertRequestDTO;
import br.dev.rodrigopinheiro.alertacomunidade.dto.AlertResponseDTO;

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
