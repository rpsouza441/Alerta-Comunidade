package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.notification;


import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.NotificationServicePort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.SubscriberRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.notification.SubscriberNotifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SubscriberNotifierTest {

    private SubscriberRepositoryPort subscriberRepository;
    private NotificationServicePort notificationService;
    private SubscriberNotifier notifier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        subscriberRepository = mock(SubscriberRepositoryPort.class);
        notificationService = mock(NotificationServicePort.class);
        notifier = new SubscriberNotifier(subscriberRepository, notificationService);
    }

    @Test
    void shouldSendNotificationsWhenContactsValid() {
        Subscriber sub = new Subscriber();
        sub.setEmail("user@test.com");
        sub.setPhoneNumber("+12345678901");
        when(subscriberRepository.findAll()).thenReturn(List.of(sub));

        AlertNotification alert = createAlert();

        notifier.notifySubscribers(alert);

        verify(notificationService).sendEmail(eq("user@test.com"), anyString(), contains(alert.getMessage()));
        verify(notificationService).sendSms(eq("+12345678901"), contains(alert.getMessage()));
    }

    @Test
    void shouldSkipInvalidContacts() {
        Subscriber sub = new Subscriber();
        sub.setEmail("invalid-email");
        sub.setPhoneNumber("12345");
        when(subscriberRepository.findAll()).thenReturn(List.of(sub));

        AlertNotification alert = createAlert();

        notifier.notifySubscribers(alert);

        verify(notificationService, never()).sendEmail(anyString(), anyString(), anyString());
        verify(notificationService, never()).sendSms(anyString(), anyString());
    }

    private AlertNotification createAlert() {
        AlertNotification alert = new AlertNotification();
        alert.setId(1L);
        alert.setMessage("Teste");
        alert.setOrigin("INMET");
        alert.setAlertType(AlertType.FIRE);
        alert.setStatus(AlertStatus.RECEIVED);
        alert.setCreatedAt(LocalDateTime.now());
        return alert;
    }
}
