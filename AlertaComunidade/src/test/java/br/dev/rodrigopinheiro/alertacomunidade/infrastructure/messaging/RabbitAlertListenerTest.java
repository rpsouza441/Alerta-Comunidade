package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.messaging;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.AlertProcessingException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.FailedAlertRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.NotificationServicePort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.SubscriberRepositoryPort;
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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RabbitAlertListenerTest {

    @Mock
    private AlertRepositoryPort alertRepository;

    @Mock
    private FailedAlertRepositoryPort failedARepository;

    @Mock
    private SubscriberRepositoryPort subscriberRepository;

    @Mock
    private NotificationServicePort notificationService;

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
        Subscriber s = new Subscriber();
        s.setEmail("a@a.com");
        s.setPhoneNumber("+11111111111");
        when(subscriberRepository.findAll()).thenReturn(java.util.List.of(s));

        //when
        listener.receiveCritical(alert);

        //then
        verify(alertRepository).save(alertCaptor.capture());
        verify(notificationService).sendEmail("a@a.com", "Alerta: " + alert.getAlertType(), alert.getMessage());
        verify(notificationService).sendSms("+11111111111", alert.getMessage());

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
        Subscriber s = new Subscriber();
        s.setEmail("b@b.com");
        s.setPhoneNumber("+22222222222");
        when(subscriberRepository.findAll()).thenReturn(java.util.List.of(s));

        //when
        listener.receiveNormal(alert);

        //then
        verify(alertRepository).save(alertCaptor.capture());
        verify(notificationService).sendEmail("b@b.com", "Alerta: " + alert.getAlertType(), alert.getMessage());
        verify(notificationService).sendSms("+22222222222", alert.getMessage());

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
        s.setEmail("c@c.com");
        s.setPhoneNumber("+33333333333");
        when(subscriberRepository.findAll()).thenReturn(java.util.List.of(s));

        //when
        listener.receiveNormal(alert);

        //then
        verify(alertRepository).save(alertCaptor.capture());
        verify(notificationService).sendEmail("c@c.com", "Alerta: " + alert.getAlertType(), alert.getMessage());
        verify(notificationService).sendSms("+33333333333", alert.getMessage());

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
