package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.application.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.output.AlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.application.dto.AlertResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

public class GetAllAlertsUseCaseTest {

    private AlertRepositoryPort alertRepository;
    private GetAllAlertsUseCase useCase;

    @BeforeEach
    void setup() {
        alertRepository = mock(AlertRepositoryPort.class);
        useCase = new GetAllAlertsUseCase(alertRepository);
    }

    @Test
    void shouldReturnAllAlerts() {
        Pageable pageable = PageRequest.of(0, 10);

        List<AlertNotification> listAlert= Stream.of(
                createValidAlert(1L, "Incendio museu"),
                createValidAlert(2L, "Incendio casa"),
                createValidAlert(3L, "Incendio mata")
        ).collect(Collectors.toList());

        Page<AlertNotification> page = new PageImpl<>(listAlert, pageable, listAlert.size());

        when(alertRepository.findAll(pageable)).thenReturn(page);

        Page<AlertResponseDTO> result = useCase.getAllAlerts(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isEqualTo(3);


        assertThat(result.getContent())
                .extracting(AlertResponseDTO::getMessage)
                .containsExactly("Incendio museu", "Incendio casa", "Incendio mata");

        verify(alertRepository, times(1)).findAll(pageable);

    }

    @Test
    void shouldReturnEmptyWhenIdDoesNotExist() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 10);
        Page<AlertNotification> emptyPage = new PageImpl<>(List.of(), pageable, 0);

        when(alertRepository.findAll(pageable)).thenReturn(emptyPage);

        Page<AlertResponseDTO> result = useCase.getAllAlerts(pageable);

        assertThat(result).isNotNull();
        assertThat(result.getTotalElements()).isZero();
        assertThat(result.getContent()).isEmpty();

        verify(alertRepository, times(1)).findAll(pageable);
    }

    private static AlertNotification createValidAlert(Long id, String message) {
        AlertNotification alert = new AlertNotification();
        alert.setMessage(message);
        alert.setOrigin("INMET");
        alert.setAlertType(AlertType.FIRE);
        alert.setStatus(AlertStatus.RECEIVED);
        alert.setCreatedAt(LocalDateTime.now());
        return alert;
    }
}
