package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;


import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.DeadLetterMessageRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.DeadLetterRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProcessQuarantinedMessageUseCaseTest {

    private DeadLetterMessageRepositoryPort repository;
    private ProcessDeadLetterMessageUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(DeadLetterMessageRepositoryPort.class);
        useCase = new ProcessDeadLetterMessageUseCase(repository);
    }

    @Test
    void shouldPersistMessage() {
        DeadLetterMessage saved = new DeadLetterMessage();
        saved.setId(1L);
        when(repository.save(any(DeadLetterMessage.class))).thenReturn(saved);

        DeadLetterMessage result = useCase.execute(new DeadLetterRequestDTO("dlq","body","{}"));

        verify(repository).save(any(DeadLetterMessage.class));
        assertThat(result.getId()).isEqualTo(1L);
    }
}