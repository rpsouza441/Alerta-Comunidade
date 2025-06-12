package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.ResourceNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.AlertResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GetAlertByIdUseCaseTest {

    private AlertRepositoryPort alertRepository;
    private GetAlertByIdUseCase useCase;

    @BeforeEach
    void setup() {
        alertRepository = mock(AlertRepositoryPort.class);
        useCase = new GetAlertByIdUseCase(alertRepository);
    }

    @Test
    void shouldReturnAlertWhenIdExists() {
        // Arrange
        Long id = 1L;
        AlertNotification alert = new AlertNotification(
                id,
                "Incêndio na mata",
                "INMET",
                AlertType.FIRE,
                LocalDateTime.now(),
                AlertStatus.RECEIVED
        );

        when(alertRepository.findById(id)).thenReturn(Optional.of(alert));

        // Act
        AlertResponseDTO result = useCase.getAlertById(id);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getMessage()).isEqualTo("Incêndio na mata");
        verify(alertRepository, times(1)).findById(id);
    }

    @Test
    void shouldReturnEmptyWhenIdDoesNotExist() {
        // Arrange
        Long id = 999L;
        when(alertRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThatThrownBy(() -> useCase.getAlertById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(String.valueOf(id));

        verify(alertRepository, times(1)).findById(id);
    }
}
