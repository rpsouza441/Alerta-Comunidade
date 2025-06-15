package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.ResourceNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.SubscriberRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ProcessSubscriberUseCaseTest {

    private SubscriberRepositoryPort repository;
    private ProcessSubscriberUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(SubscriberRepositoryPort.class);
        useCase = new ProcessSubscriberUseCase(repository);
    }

    @Test
    void shouldDeactivateSubscriber() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(1L);
        subscriber.setActive(true);

        when(repository.findById(1L)).thenReturn(Optional.of(subscriber));

        useCase.deactivate(1L);

        verify(repository).save(subscriber);
        assert !subscriber.isActive();
    }

    @Test
    void shouldThrowWhenSubscriberNotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.deactivate(2L))
                .isInstanceOf(ResourceNotFoundException.class);
        verify(repository, never()).save(any());
    }
}