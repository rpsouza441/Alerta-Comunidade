package br.dev.rodrigpinheiro.AlertaComunidade.mapper;

import br.dev.rodrigpinheiro.AlertaComunidade.dto.AlertRequestDTO;
import br.dev.rodrigpinheiro.AlertaComunidade.dto.AlertResponseDTO;
import br.dev.rodrigpinheiro.AlertaComunidade.enums.AlertStatus;
import br.dev.rodrigpinheiro.AlertaComunidade.model.AlertNotification;

public class AlertMapper {


    /**
     * Converte um DTO de entrada em uma entidade do domínio,
     * aplicando as regras padrão como status inicial RECEIVED.
     *
     * @param dto objeto recebido da API
     * @return entidade pronta para persistência
     */
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
