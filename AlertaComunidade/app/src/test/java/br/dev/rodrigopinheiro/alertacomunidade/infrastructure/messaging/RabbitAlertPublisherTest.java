package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.messaging;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.config.RabbitMQConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class RabbitAlertPublisherTest {

    private RabbitTemplate rabbitTemplate;
    private RabbitAlertPublisher rabbitAlertPublisher;

    @BeforeEach
    public void setUp() {
        rabbitTemplate = mock(RabbitTemplate.class);
        rabbitAlertPublisher = new RabbitAlertPublisher(rabbitTemplate);
    }


    @Test
    void shouldSendCriticalAlert() {
        //given
        AlertNotification alert = createAlert(1L, "Incendio", "INMET", AlertType.FIRE, AlertStatus.RECEIVED);

        //hen
        rabbitAlertPublisher.publish(alert);

        //then
        verifyPublish(RabbitMQConfig.CRITICAL_ROUTING_KEY, alert);
    }

    @Test
    void shouldSendNormalAlert() {
        //given
        AlertNotification alert = createAlert(2L, "Chuva intensa", "CEMADEN", AlertType.WEATHER, AlertStatus.RECEIVED);

        //when
        rabbitAlertPublisher.publish(alert);

        //then
        verifyPublish(RabbitMQConfig.NORMAL_ROUTING_KEY, alert);
    }

    @Test
    void shouldSendLogAlertWhenTypeIsNull() {
        //given
        AlertNotification alert = createAlert(3L, "Alerta genérico", "Sistema", null, AlertStatus.RECEIVED);
        //when
        rabbitAlertPublisher.publish(alert);
        //then
        verifyPublish(RabbitMQConfig.LOG_ROUTING_KEY, alert);
    }

    @Test
    void shouldThrowExceptionWhenRabbitFails() {
        //given
        AlertNotification alert = createAlert(4L, "Falha envio", "Teste", AlertType.CRIME, AlertStatus.RECEIVED);


        doThrow(new AmqpException("Erro simulado"))
                .when(rabbitTemplate)
                .convertAndSend(
                        eq(RabbitMQConfig.EXCHANGE),
                        eq(RabbitMQConfig.CRITICAL_ROUTING_KEY),
                        any(AlertNotification.class)
                );

        // when + then
        assertThatThrownBy(() -> rabbitAlertPublisher.publish(alert))
                .isInstanceOf(AmqpException.class)
                .hasMessageContaining("Erro simulado");
    }

    // === Métodos utilitários ===

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

    private void verifyPublish(String expectedRoutingKey, AlertNotification expectedAlert) {
        ArgumentCaptor<String> routingKeyCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Object> payloadCaptor = ArgumentCaptor.forClass(Object.class);

        verify(rabbitTemplate).convertAndSend(eq(RabbitMQConfig.EXCHANGE), routingKeyCaptor.capture(), payloadCaptor.capture());

        String actualRoutingKey = routingKeyCaptor.getValue();
        AlertNotification actualAlert = (AlertNotification) payloadCaptor.getValue();

        assertThat(actualRoutingKey).isEqualTo(expectedRoutingKey);
        assertThat(actualAlert.getId()).isEqualTo(expectedAlert.getId());
        assertThat(actualAlert.getMessage()).isEqualTo(expectedAlert.getMessage());
        assertThat(actualAlert.getOrigin()).isEqualTo(expectedAlert.getOrigin());
        assertThat(actualAlert.getAlertType()).isEqualTo(expectedAlert.getAlertType());
        assertThat(actualAlert.getStatus()).isEqualTo(expectedAlert.getStatus());
        assertThat(actualAlert.getCreatedAt()).isEqualTo(expectedAlert.getCreatedAt());
    }
}
