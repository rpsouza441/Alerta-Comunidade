package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.application.application.mapper.AlertMapper;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.output.AlertPublisherPort;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.output.AlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.application.dto.AlertRequestDTO;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.application.usecase.ProcessAlertUseCase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class ProcessAlertUseCaseTest {

    private AlertRepositoryPort repository;
    private ProcessAlertUseCase useCase;
    private AlertPublisherPort publisher;

    @BeforeEach
    void setUp() {
        repository = mock(AlertRepositoryPort.class);
        publisher = mock(AlertPublisherPort.class);
        useCase = new ProcessAlertUseCase(repository, publisher);
    }


    @Test
    void shouldProcessAlert() {
        // Arrange
        AlertRequestDTO dto = new AlertRequestDTO("Incêndio na mata", "INMET", AlertType.FIRE);
        AlertNotification savedEntity = AlertMapper.toEntity(dto);
        savedEntity.setId(1L);

        when(repository.save(any(AlertNotification.class))).thenReturn(savedEntity);

        // Act
        useCase.processAlert(dto);

        // Assert - capturando argumentos
        ArgumentCaptor<AlertNotification> alertCaptor = ArgumentCaptor.forClass(AlertNotification.class);

        verify(repository).save(alertCaptor.capture());
        verify(publisher).publish(alertCaptor.capture());

        // Valida conteúdo do alerta
        AlertNotification saved = alertCaptor.getAllValues().get(0); // primeiro: save
        AlertNotification published = alertCaptor.getAllValues().get(1); // segundo: publish

        assertThat(saved.getMessage()).isEqualTo("Incêndio na mata");
        assertThat(saved.getOrigin()).isEqualTo("INMET");
        assertThat(saved.getAlertType()).isEqualTo(AlertType.FIRE);

        assertThat(published).isEqualTo(saved); // deve ser o mesmo objeto
    }

    @Test
    void shouldThrowExceptionWhenRepositoryFails() {
        // Arrange
        AlertRequestDTO dto = new AlertRequestDTO("Incêndio na mata", "INMET", AlertType.FIRE);

        when(repository.save(any(AlertNotification.class))).thenThrow(new RuntimeException("Erro de persistência"));

        // Act + Assert
        assertThatThrownBy(() -> useCase.processAlert(dto))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Erro de persistência");

        verify(repository, times(1)).save(any(AlertNotification.class));
        verify(publisher, never()).publish(any());
    }
}
