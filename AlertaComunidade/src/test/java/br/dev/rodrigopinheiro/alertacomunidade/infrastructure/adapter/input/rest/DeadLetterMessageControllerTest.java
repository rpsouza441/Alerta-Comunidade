package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.input.rest;

import br.dev.rodrigopinheiro.alertacomunidade.domain.model.DeadLetterMessage;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAllQuarantinedMessagesInputPort;
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
@Import(DeadLetterMessageControllerTest.TestConfig.class)
class DeadLetterMessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @Qualifier("getAllQuarantinedMessagesUseCase")
    private GetAllQuarantinedMessagesInputPort getAllUseCase;

    @TestConfiguration
    static class TestConfig {
        @Bean(name = "getAllQuarantinedMessagesUseCase")
        public GetAllQuarantinedMessagesInputPort useCase() {
            return mock(GetAllQuarantinedMessagesInputPort.class);
        }
    }

    @Test
    void shouldReturnMessages() throws Exception {
        DeadLetterMessage msg = new DeadLetterMessage();
        msg.setId(1L);
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<DeadLetterMessage> page = new PageImpl<>(List.of(msg), pageRequest, 1);
        when(getAllUseCase.getAll(pageRequest)).thenReturn(page);

        mockMvc.perform(get("/dead-letters").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }
}