package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class GetAllFailedAlertsUseCaseTest {

    private FailedAlertRepositoryPort repository;
    private GetAllFailedAlertsUseCase useCase;

    @BeforeEach
    void setup() {
        repository = mock(FailedAlertRepositoryPort.class);
        useCase = new GetAllFailedAlertsUseCase(repository);
    }

    @Test
    void shouldReturnPagedFailedAlerts() {
        Pageable pageable = PageRequest.of(0, 10);
        FailedAlertNotification failed = createFailed("F1");
        Page<FailedAlertNotification> page = new PageImpl<>(List.of(failed), pageable, 1);

        when(repository.findAll(pageable)).thenReturn(page);

        Page<FailedAlertNotification> result = useCase.getAllFailedAlerts(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent()).containsExactly(failed);
        verify(repository).findAll(pageable);
    }

    private FailedAlertNotification createFailed(String message) {
        FailedAlertNotification failed = new FailedAlertNotification();
        failed.setOriginalId(10L);
        failed.setMessage(message);
        failed.setOrigin("INMET");
        failed.setAlertType(AlertType.FIRE);
        failed.setStatus(AlertStatus.FAILED);
        failed.setCreatedAt(LocalDateTime.now());
        failed.setErrorMessage("Falha");
        failed.setFailedAt(LocalDateTime.now());
        return failed;
    }
}