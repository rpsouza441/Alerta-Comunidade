package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.FailedAlertNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertPublisherPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.Mockito.*;

public class ReprocessFailedAlertUseCaseTest {

    private FailedAlertRepositoryPort failedRepository;
    private AlertPublisherPort publisher;
    private ReprocessFailedAlertUseCase useCase;

    @BeforeEach
    void setUp() {
        failedRepository = mock(FailedAlertRepositoryPort.class);
        publisher = mock(AlertPublisherPort.class);
        useCase = new ReprocessFailedAlertUseCase(failedRepository, publisher);
    }

    @Test
    void shouldReprocessFailedAlertSuccessfully() {
        // given
        Long id = 42L;
        FailedAlertNotification failed = new FailedAlertNotification();
        failed.setId(id);
        failed.setOriginalId(100L);
        failed.setMessage("Alerta falhou");
        failed.setOrigin("INMET");
        failed.setAlertType(AlertType.FIRE);
        failed.setStatus(AlertStatus.FAILED);
        failed.setCreatedAt(LocalDateTime.now());

        when(failedRepository.findById(id)).thenReturn(Optional.of(failed));

        // when
        useCase.execute(id);

        // then
        // Captura o alerta publicado
        ArgumentCaptor<AlertNotification> alertCaptor = ArgumentCaptor.forClass(AlertNotification.class);
        verify(publisher).publish(alertCaptor.capture());

        AlertNotification alertSent = alertCaptor.getValue();
        assertThat(alertSent.getMessage()).isEqualTo(failed.getMessage());
        assertThat(alertSent.getOrigin()).isEqualTo(failed.getOrigin());
        assertThat(alertSent.getAlertType()).isEqualTo(failed.getAlertType());
        assertThat(alertSent.getStatus()).isEqualTo(AlertStatus.RECEIVED);
        assertThat(alertSent.getCreatedAt()).isEqualTo(failed.getCreatedAt());

        // Captura o objeto salvo como reprocessado
        ArgumentCaptor<FailedAlertNotification> failedCaptor = ArgumentCaptor.forClass(FailedAlertNotification.class);
        verify(failedRepository).save(failedCaptor.capture());

        FailedAlertNotification updated = failedCaptor.getValue();
        assertThat(updated.getStatus()).isEqualTo(AlertStatus.REPROCESSED);
        assertThat(updated.getReprocessedAt()).isNotNull();



    }


    @Test
    void shouldThrowWhenFailedAlertDoesNotExist() {
        // given
        when(failedRepository.findById(999L)).thenReturn(Optional.empty());

        // when + then
        assertThatThrownBy(() -> useCase.execute(999L))
                .isInstanceOf(FailedAlertNotFoundException.class)
                .hasMessageContaining("999");

        verify(publisher, never()).publish(any());
        verify(failedRepository, never()).save(any());
    }


}
