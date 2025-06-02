package br.dev.rodrigopinheiro.alertacomunidade.application.mapper;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.dto.AlertRequestDTO;
import br.dev.rodrigopinheiro.alertacomunidade.dto.AlertResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertMapperTest {

    @Test
    void testToEntityFromRequestDTO() {
        AlertRequestDTO dto = new AlertRequestDTO("Chuva forte", "INMET", AlertType.WEATHER);

        AlertNotification entity = AlertMapper.toEntity(dto);
        assertNotNull(entity, "A entidade convertida não pode ser nula");

        assertEquals("Chuva forte", entity.getMessage());
        assertEquals("INMET", entity.getOrigin());
        assertEquals(AlertType.WEATHER, entity.getAlertType());
        assertEquals(AlertStatus.RECEIVED, entity.getStatus());
        //"createdAt deve estar preenchido via @PrePersist ou manualmente"
        //assertNotNull(entity.getCreatedAt());
    }

    @Test
    void testToResponseDTOFromEntity() {
        AlertNotification entity = new AlertNotification();
        entity.setId(123L);
        entity.setMessage("Incêndio");
        entity.setOrigin("DEFESA_CIVIL");
        entity.setAlertType(AlertType.FIRE);
        entity.setStatus(AlertStatus.SENT_TO_QUEUE);

        AlertResponseDTO dto = AlertMapper.toResponseDTO(entity);

        assertNotNull(dto);
        assertEquals(123L, dto.getId());
        assertEquals("Incêndio", dto.getMessage());
        assertEquals("DEFESA_CIVIL", dto.getOrigin());
        assertEquals(AlertType.FIRE, dto.getAlertType());
        assertEquals(AlertStatus.SENT_TO_QUEUE, dto.getAlertStatus());
    }
}