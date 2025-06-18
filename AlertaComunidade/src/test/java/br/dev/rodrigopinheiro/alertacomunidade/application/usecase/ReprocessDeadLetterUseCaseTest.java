package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;


import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.DeadLetterStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.DeadLetterMessageNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.AlertPublisherPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.DeadLetterRepositoryPort;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReprocessDeadLetterUseCaseTest {

    private DeadLetterRepositoryPort repository;
    private AlertPublisherPort publisher;
    private ObjectMapper mapper;
    private ReprocessDeadLetterUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(DeadLetterRepositoryPort.class);
        publisher = mock(AlertPublisherPort.class);
        mapper = new ObjectMapper();
        useCase = new ReprocessDeadLetterUseCase(repository, publisher, mapper);
    }

    @Test
    void shouldReprocessMessage() throws Exception {
        Long id = 1L;
        AlertNotification alert = new AlertNotification();
        alert.setMessage("msg");
        alert.setOrigin("INMET");
        alert.setAlertType(AlertType.FIRE);
        alert.setStatus(AlertStatus.RECEIVED);
        alert.setCreatedAt(LocalDateTime.now());

        DeadLetterMessage msg = new DeadLetterMessage();
        msg.setId(id);
        msg.setQueueName("alerts");
        msg.setPayload(mapper.writeValueAsString(alert));
        msg.setHeaders("{}");
        msg.setStatus(DeadLetterStatus.PENDING);

        when(repository.findById(id)).thenReturn(Optional.of(msg));

        useCase.execute(id);

        ArgumentCaptor<AlertNotification> captor = ArgumentCaptor.forClass(AlertNotification.class);
        verify(publisher).publish(captor.capture());
        assertThat(captor.getValue().getMessage()).isEqualTo("msg");
        verify(repository).save(msg);
    }

    @Test
    void shouldThrowWhenMessageNotFound() {
        when(repository.findById(99L)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> useCase.execute(99L))
                .isInstanceOf(DeadLetterMessageNotFoundException.class);
        verifyNoInteractions(publisher);
    }
}