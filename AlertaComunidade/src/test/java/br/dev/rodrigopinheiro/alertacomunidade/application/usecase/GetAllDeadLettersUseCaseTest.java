package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.DeadLetterStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.DeadLetterRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class GetAllDeadLettersUseCaseTest {

    private DeadLetterRepositoryPort repository;
    private GetAllDeadLettersUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(DeadLetterRepositoryPort.class);
        useCase = new GetAllDeadLettersUseCase(repository);
    }

    @Test
    void shouldReturnMessages() {
        Pageable pageable = PageRequest.of(0, 10);
        DeadLetterMessage msg = new DeadLetterMessage();
        msg.setId(1L);
        msg.setStatus(DeadLetterStatus.PENDING);
        Page<DeadLetterMessage> page = new PageImpl<>(List.of(msg), pageable, 1);
        when(repository.findAllByStatus(DeadLetterStatus.PENDING, pageable)).thenReturn(page);

        Page<DeadLetterMessage> result = useCase.getAll(DeadLetterStatus.PENDING, pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(repository).findAllByStatus(DeadLetterStatus.PENDING, pageable);
    }
}