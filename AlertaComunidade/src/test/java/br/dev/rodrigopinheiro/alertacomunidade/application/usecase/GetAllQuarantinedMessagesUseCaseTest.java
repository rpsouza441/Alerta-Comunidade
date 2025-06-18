package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.DeadLetterMessageRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GetAllQuarantinedMessagesUseCaseTest {

    private DeadLetterMessageRepositoryPort repository;
    private GetAllDeadLetterMessagesUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(DeadLetterMessageRepositoryPort.class);
        useCase = new GetAllDeadLetterMessagesUseCase(repository);
    }

    @Test
    void shouldReturnMessages() {
        Pageable pageable = PageRequest.of(0, 10);
        DeadLetterMessage msg = new DeadLetterMessage();
        msg.setId(1L);
        Page<DeadLetterMessage> page = new PageImpl<>(List.of(msg), pageable, 1);
        when(repository.findAll(pageable)).thenReturn(page);

        Page<DeadLetterMessage> result = useCase.getAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(repository).findAll(pageable);
    }
}