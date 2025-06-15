package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.Subscriber;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.SubscriberRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GetAllSubscribersUseCaseTest {

    private SubscriberRepositoryPort repository;
    private GetAllSubscribersUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(SubscriberRepositoryPort.class);
        useCase = new GetAllSubscribersUseCase(repository);
    }

    @Test
    void shouldReturnAllSubscribers() {
        Subscriber s1 = new Subscriber();
        s1.setId(1L);
        s1.setEmail("a@example.com");
        s1.setActive(true);
        Subscriber s2 = new Subscriber();
        s2.setId(2L);
        s2.setEmail("b@example.com");
        s2.setActive(false);

        when(repository.findAll()).thenReturn(Stream.of(s1, s2).toList());

        List<SubscriberResponseDTO> result = useCase.getAll();

        assertThat(result).hasSize(2);
        assertThat(result).extracting(SubscriberResponseDTO::getId)
                .containsExactly(1L, 2L);
        verify(repository, times(1)).findAll();
    }
}