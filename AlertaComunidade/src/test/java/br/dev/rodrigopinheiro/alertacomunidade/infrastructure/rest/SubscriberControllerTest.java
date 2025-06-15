package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.rest;


import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.ResourceNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAllSubscribersInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetSubscriberByIdInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ProcessSubscriberInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.RegisterSubscriberInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.SubscriberResponseDTO;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(SubscriberController.class)
@Import(SubscriberControllerTest.TestConfig.class)
class SubscriberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @Qualifier("registerSubscriberInputPort")
    private RegisterSubscriberInputPort registerSubscriberInputPort;

    @Autowired
    @Qualifier("getAllSubscribersInputPort")
    private GetAllSubscribersInputPort getAllSubscribersInputPort;

    @Autowired
    @Qualifier("getSubscriberByIdInputPort")
    private GetSubscriberByIdInputPort getSubscriberByIdInputPort;

    @Autowired
    @Qualifier("processSubscriberInputPort")
    private ProcessSubscriberInputPort processSubscriberInputPort;

    @TestConfiguration
    static class TestConfig {
        @Bean(name = "registerSubscriberInputPort")
        public RegisterSubscriberInputPort registerSubscriberInputPort() {
            return mock(RegisterSubscriberInputPort.class);
        }
        @Bean(name = "getAllSubscribersInputPort")
        public GetAllSubscribersInputPort getAllSubscribersInputPort() {
            return mock(GetAllSubscribersInputPort.class);
        }
        @Bean(name = "getSubscriberByIdInputPort")
        public GetSubscriberByIdInputPort getSubscriberByIdInputPort() {
            return mock(GetSubscriberByIdInputPort.class);
        }
        @Bean(name = "processSubscriberInputPort")
        public ProcessSubscriberInputPort processSubscriberInputPort() {
            return mock(ProcessSubscriberInputPort.class);
        }
    }

    @Test
    void shouldReturnAllSubscribers() throws Exception {
        SubscriberResponseDTO dto = new SubscriberResponseDTO(1L, "a@test.com", "+11111111111", true);
        Page<SubscriberResponseDTO> page = new PageImpl<>(List.of(dto));
        when(getAllSubscribersInputPort.getAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/subscribers?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1L));
    }


    @Test
    void shouldReturnSubscriberById() throws Exception {
        SubscriberResponseDTO dto = new SubscriberResponseDTO(2L, "b@test.com", "+11111111112", true);
        when(getSubscriberByIdInputPort.getById(2L)).thenReturn(dto);

        mockMvc.perform(get("/subscribers/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L));
    }

    @Test
    void shouldReturn404WhenSubscriberNotFound() throws Exception {
        when(getSubscriberByIdInputPort.getById(3L)).thenThrow(new ResourceNotFoundException("not"));

        mockMvc.perform(get("/subscribers/3"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldActivateSubscriber() throws Exception {
        mockMvc.perform(post("/subscribers/4/activate"))
                .andExpect(status().isOk());
        verify(processSubscriberInputPort).activate(4L);
    }

    @Test
    void shouldDeactivateSubscriber() throws Exception {
        mockMvc.perform(post("/subscribers/5/deactivate"))
                .andExpect(status().isOk());
        verify(processSubscriberInputPort).deactivate(5L);
    }
}