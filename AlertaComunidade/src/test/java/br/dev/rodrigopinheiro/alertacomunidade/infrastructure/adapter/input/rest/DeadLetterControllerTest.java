package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.input.rest;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.DeadLetterStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAllDeadLettersInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ReprocessDeadLetterInputPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(DeadLetterController.class)
@Import(DeadLetterControllerTest.TestConfig.class)
class DeadLetterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @Qualifier("getAllDeadLetterUseCase")
    private GetAllDeadLettersInputPort getAllUseCase;

    @Autowired
    @Qualifier("reprocessDeadLetterUseCase")
    private ReprocessDeadLetterInputPort reprocessDeadLetterUseCase;

    @TestConfiguration
    static class TestConfig {
        @Bean(name = "getAllDeadLetterUseCase")
        public GetAllDeadLettersInputPort getAllUseCase() {
            return mock(GetAllDeadLettersInputPort.class);

        }

        @Bean(name = "reprocessDeadLetterUseCase")
        public ReprocessDeadLetterInputPort reprocessDeadLetterUseCase() {
            return mock(ReprocessDeadLetterInputPort.class);

        }


    }

    @Test
    void shouldReturnMessages() throws Exception {
        DeadLetterMessage msg = new DeadLetterMessage();
        msg.setId(1L);
        msg.setStatus(DeadLetterStatus.PENDING);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<DeadLetterMessage> page = new PageImpl<>(List.of(msg), pageRequest, 1);
        when(getAllUseCase.getAll(DeadLetterStatus.PENDING, pageRequest)).thenReturn(page);

        mockMvc.perform(get("/dead-letters").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }

    @Test
    void shouldReturnProcessedMessages() throws Exception {
        DeadLetterMessage msg = new DeadLetterMessage();
        msg.setId(1L);
        msg.setStatus(DeadLetterStatus.REPROCESSED);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<DeadLetterMessage> page = new PageImpl<>(List.of(msg), pageRequest, 1);
        when(getAllUseCase.getAll(DeadLetterStatus.REPROCESSED, pageRequest)).thenReturn(page);

        mockMvc.perform(get("/dead-letters/processed").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].status").value("REPROCESSED"));
    }
}