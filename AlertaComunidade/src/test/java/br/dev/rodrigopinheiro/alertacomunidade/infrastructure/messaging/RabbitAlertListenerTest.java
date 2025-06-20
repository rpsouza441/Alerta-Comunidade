package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.input.messaging;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.AlertProcessingException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ProcessFailedAlertInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.BackupStoragePort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.input.messaging.RabbitAlertListener;
import br.dev.rodrigopinheiro.alertacomunidade.application.service.SubscriberNotifier;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RabbitAlertListenerTest {

    @Mock
    private AlertRepositoryPort alertRepository;

    @Mock
    private ProcessFailedAlertInputPort processFailedAlertUseCasePort;

    @Mock
    private SubscriberNotifier subscriberNotifier;

    @Mock
    private BackupStoragePort backupStoragePort;

    @InjectMocks
    private RabbitAlertListener listener;

    @Captor
    private ArgumentCaptor<AlertNotification> alertCaptor;

    @Captor
    private ArgumentCaptor<String> errorCaptor;

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
        verify(subscriberNotifier).notifySubscribers(alert);


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
        verify(subscriberNotifier).notifySubscribers(alert);


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
        Subscriber s = new Subscriber();


        //when
        listener.receiveNormal(alert);

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
        verify(processFailedAlertUseCasePort)
                .execute(alertCaptor.capture(), errorCaptor.capture());
        verify(backupStoragePort, never()).save(any(), any());

        assertThat(alertCaptor.getValue()).isSameAs(alert);
        assertThat(errorCaptor.getValue()).contains("Banco indisponível");
    }

    @Test
    void shouldBackupAlertWhenParkingLotFails() {
        // given
        AlertNotification alert = createAlert(
                99L,
                "Falha crítica",
                "Sistema",
                AlertType.CRIME,
                AlertStatus.RECEIVED
        );

        AlertProcessingException exception = new AlertProcessingException("Banco indisponível");
        doThrow(new RuntimeException("DLQ down"))
                .when(processFailedAlertUseCasePort).execute(any(), any());

        // when
        listener.recover(exception, alert);

        // then
        verify(backupStoragePort).save(errorCaptor.capture(), alertCaptor.capture());
        assertThat(alertCaptor.getValue()).isSameAs(alert);
        assertThat(errorCaptor.getValue()).contains("Banco indisponível");
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
