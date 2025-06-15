package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.ResourceNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.SubscriberRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;

class GetSubscriberByIdUseCaseTest {

    private SubscriberRepositoryPort repository;
    private GetSubscriberByIdUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(SubscriberRepositoryPort.class);
        useCase = new GetSubscriberByIdUseCase(repository);
    }

    @Test
    void shouldReturnSubscriberWhenExists() {
        Subscriber subscriber = new Subscriber();
        subscriber.setId(1L);
        subscriber.setEmail("test@example.com");
        subscriber.setPhoneNumber("+5511999999999");
        subscriber.setActive(true);

        when(repository.findById(1L)).thenReturn(Optional.of(subscriber));

        SubscriberResponseDTO result = useCase.getById(1L);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.isActive()).isTrue();
        verify(repository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowWhenSubscriberNotFound() {
        when(repository.findById(2L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.getById(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("2");
        verify(repository, times(1)).findById(2L);
    }
}