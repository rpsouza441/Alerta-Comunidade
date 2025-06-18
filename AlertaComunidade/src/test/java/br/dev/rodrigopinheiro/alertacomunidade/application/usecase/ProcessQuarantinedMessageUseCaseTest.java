package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;


import br.dev.rodrigopinheiro.alertacomunidade.domain.model.QuarantinedMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.QuarantinedMessageRepositoryPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.QuarantinedMessageRequestDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ProcessQuarantinedMessageUseCaseTest {

    private QuarantinedMessageRepositoryPort repository;
    private ProcessQuarantinedMessageUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(QuarantinedMessageRepositoryPort.class);
        useCase = new ProcessQuarantinedMessageUseCase(repository);
    }

    @Test
    void shouldPersistMessage() {
        QuarantinedMessage saved = new QuarantinedMessage();
        saved.setId(1L);
        when(repository.save(any(QuarantinedMessage.class))).thenReturn(saved);

        QuarantinedMessage result = useCase.execute(new QuarantinedMessageRequestDTO("dlq","body","{}"));

        verify(repository).save(any(QuarantinedMessage.class));
        assertThat(result.getId()).isEqualTo(1L);
    }
}