package br.dev.rodrigopinheiro.alertacomunidade.application.mapper;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.dto.AlertRequestDTO;
import br.dev.rodrigopinheiro.alertacomunidade.dto.AlertResponseDTO;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


class AlertMapperTest {
    private static  Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldMapRequestDtoToEntity() {
        AlertRequestDTO dto = new AlertRequestDTO("Chuva forte", "INMET", AlertType.WEATHER);

        AlertNotification entity = AlertMapper.toEntity(dto);

        assertThat(entity).isNotNull();
        assertThat(entity.getMessage()).isEqualTo("Chuva forte");
        assertThat(entity.getOrigin()).isEqualTo("INMET");
        assertThat(entity.getAlertType()).isEqualTo(AlertType.WEATHER);
        assertThat(entity.getStatus()).isEqualTo(AlertStatus.RECEIVED);
        // Caso `createdAt` seja atribuído no mapper:
        // assertThat(entity.getCreatedAt()).isNotNull();
    }

    @Test
    void shouldMapEntityToResponseDto() {
        AlertNotification entity = new AlertNotification();
        entity.setId(123L);
        entity.setMessage("Incêndio");
        entity.setOrigin("DEFESA_CIVIL");
        entity.setAlertType(AlertType.FIRE);
        entity.setStatus(AlertStatus.SENT_TO_QUEUE);

        AlertResponseDTO dto = AlertMapper.toResponseDTO(entity);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(123L);
        assertThat(dto.getMessage()).isEqualTo("Incêndio");
        assertThat(dto.getOrigin()).isEqualTo("DEFESA_CIVIL");
        assertThat(dto.getAlertType()).isEqualTo(AlertType.FIRE);
        assertThat(dto.getAlertStatus()).isEqualTo(AlertStatus.SENT_TO_QUEUE);
    }

    @Test
    void shouldFailValidationWhenFieldsAreInvalid() {
        AlertRequestDTO dto = new AlertRequestDTO();
        dto.setMessage(" "); // inválido
        dto.setOrigin("abc"); // inválido (minúsculo, < 4 chars)
        dto.setAlertType(null); // inválido

        Set<ConstraintViolation<AlertRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).hasSize(3);
        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .containsExactlyInAnyOrder("message", "origin", "alertType");
    }

    @Test
    void shouldPassValidationWhenFieldsAreValid() {
        AlertRequestDTO dto = new AlertRequestDTO();
        dto.setMessage("CHUVA");
        dto.setOrigin("INMET");
        dto.setAlertType(AlertType.WEATHER);

        Set<ConstraintViolation<AlertRequestDTO>> violations = validator.validate(dto);

        assertThat(violations).isEmpty();
    }


}
