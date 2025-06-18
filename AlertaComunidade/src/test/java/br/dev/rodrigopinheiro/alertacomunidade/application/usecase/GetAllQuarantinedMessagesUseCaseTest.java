package br.dev.rodrigopinheiro.alertacomunidade.application.usecase;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.QuarantinedMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.output.QuarantinedMessageRepositoryPort;
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

    private QuarantinedMessageRepositoryPort repository;
    private GetAllQuarantinedMessagesUseCase useCase;

    @BeforeEach
    void setUp() {
        repository = mock(QuarantinedMessageRepositoryPort.class);
        useCase = new GetAllQuarantinedMessagesUseCase(repository);
    }

    @Test
    void shouldReturnMessages() {
        Pageable pageable = PageRequest.of(0, 10);
        QuarantinedMessage msg = new QuarantinedMessage();
        msg.setId(1L);
        Page<QuarantinedMessage> page = new PageImpl<>(List.of(msg), pageable, 1);
        when(repository.findAll(pageable)).thenReturn(page);

        Page<QuarantinedMessage> result = useCase.getAll(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(repository).findAll(pageable);
    }
}