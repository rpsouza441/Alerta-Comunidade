package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.messaging;

import br.dev.rodrigopinheiro.alertacomunidade.common.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.exception.AlertProcessingException;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.output.AlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.port.output.FailedAlertRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class RabbitAlertListenerTest {

    @Mock
    private AlertRepositoryPort alertRepository;

    @Mock
    private FailedAlertRepositoryPort failedARepository;

    @InjectMocks
    private RabbitAlertListener listener;

    @Captor
    private ArgumentCaptor<AlertNotification> alertCaptor;

    @Captor
    private ArgumentCaptor<FailedAlertNotification> failedCaptor;

    @Test
    void shouldProcessCriticalAlertSuccessfully() {
        //given
        AlertNotification alert = createAlert(
                1L,
                "Incendio",
                "INMET",
                AlertType.FIRE,
                AlertStatus.RECEIVED
        );

        //when
        listener.receiveCritical(alert);

        //then
        verify(alertRepository).save(alertCaptor.capture());

        AlertNotification saved = alertCaptor.getValue();
        verifyListener(saved, alert);

    }

    @Test
    void shouldProcessNormalAlertSuccessfully() {
        //given
        AlertNotification alert = createAlert(
                2L,
                "Vacinacao",
                "INMET",
                AlertType.MEDICAL,
                AlertStatus.RECEIVED
        );

        //when
        listener.receiveNormal(alert);

        //then
        verify(alertRepository).save(alertCaptor.capture());

        AlertNotification saved = alertCaptor.getValue();
        verifyListener(saved, alert);

    }

    @Test
    void shouldProcessLogAlertSuccessfully() {
        //given
        AlertNotification alert = createAlert(
                3L,
                "LOG",
                "INMET",
                AlertType.OTHER,
                AlertStatus.RECEIVED
        );

        //when
        listener.receiveLog(alert);

        //then
        verify(alertRepository).save(alertCaptor.capture());

        AlertNotification saved = alertCaptor.getValue();
        verifyListener(saved, alert);

    }
    @Test
    void shouldPersistFailedAlertWithProperDataInRecover() {
        // given
        AlertNotification alert = createAlert(
                99L,
                "Falha crítica",
                "Sistema",
                AlertType.CRIME,
                AlertStatus.RECEIVED
        );

        Throwable rootCause = new RuntimeException("Erro real do banco");
        AlertProcessingException exception = new AlertProcessingException("Banco indisponível", rootCause);


        // when
        listener.recover(exception, alert);

        // then
        verify(failedARepository).save(failedCaptor.capture());

        FailedAlertNotification failed = failedCaptor.getValue();
        assertThat(failed.getOriginalId()).isEqualTo(alert.getId());
        assertThat(failed.getMessage()).isEqualTo(alert.getMessage());
        assertThat(failed.getOrigin()).isEqualTo(alert.getOrigin());
        assertThat(failed.getAlertType()).isEqualTo(alert.getAlertType());
        assertThat(failed.getStatus()).isEqualTo(AlertStatus.FAILED);
        assertThat(failed.getErrorMessage()).contains("Banco indisponível");
    }

    private static void verifyListener(AlertNotification saved, AlertNotification alert) {
        assertThat(saved.getId()).isEqualTo(alert.getId());
        assertThat(saved.getStatus()).isEqualTo(AlertStatus.DELIVERED);
        assertThat(saved.getMessage()).isEqualTo(alert.getMessage());
        assertThat(saved.getOrigin()).isEqualTo(alert.getOrigin());
        assertThat(saved.getAlertType()).isEqualTo(alert.getAlertType());
    }

    private AlertNotification createAlert(Long id, String message, String origin, AlertType type, AlertStatus status) {
        AlertNotification alert = new AlertNotification();
        alert.setId(id);
        alert.setMessage(message);
        alert.setOrigin(origin);
        alert.setAlertType(type);
        alert.setStatus(status);
        alert.setCreatedAt(LocalDateTime.now());
        return alert;
    }
}
