package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.input.rest;

import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.FailedAlertNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAllFailedAlertsInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ReprocessFailedAlertUseCasePort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(FailedAlertController.class)
@Import(FailedAlertControllerTest.TestConfig.class)
class FailedAlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @Qualifier("reprocessFailedAlertUseCase")
    private ReprocessFailedAlertUseCasePort useCase;

    @Autowired
    @Qualifier("getAllFailedAlertsUseCase")
    private GetAllFailedAlertsInputPort getAllFailedAlertsUseCase;

    @TestConfiguration
    static class TestConfig {
        @Bean(name = "reprocessFailedAlertUseCase")
        public ReprocessFailedAlertUseCasePort useCase() {
            return mock(ReprocessFailedAlertUseCasePort.class);
        }

        @Bean(name = "getAllFailedAlertsUseCase")
        public GetAllFailedAlertsInputPort getAllFailedAlertsUseCase() {
            return mock(GetAllFailedAlertsInputPort.class);
        } }

    @Test
    void shouldReturn200WithListOfFailedAlerts() throws Exception {
        FailedAlertNotification failed = new FailedAlertNotification();
        failed.setId(1L);
        failed.setMessage("Erro");
        failed.setOrigin("INMET");
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<FailedAlertNotification> page = new PageImpl<>(List.of(failed), pageRequest, 1);
        when(getAllFailedAlertsUseCase.getAllFailedAlerts(any(Pageable.class))).thenReturn(page);
        mockMvc.perform(get("/failed-alerts").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].message").value("Erro"))
                .andExpect(jsonPath("$.content[0].origin").value("INMET"));
    }

    @Test
    void shouldReturnEmptyListWhenNoFailedAlerts() throws Exception {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<FailedAlertNotification> empty = new PageImpl<>(Collections.emptyList(), pageRequest, 0);
        when(getAllFailedAlertsUseCase.getAllFailedAlerts(any(Pageable.class))).thenReturn(empty);

        mockMvc.perform(get("/failed-alerts").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }

    @Test
    void shouldReprocessSuccessfullyWhenValidId() throws Exception {
        doNothing().when(useCase).execute(1L);

        mockMvc.perform(post("/failed-alerts/1/reprocess"))
                .andExpect(status().isOk())
                .andExpect(content().string("Alerta reenviado com sucesso para a fila"));
    }

    @Test
    void shouldReturn404WhenAlertNotFound() throws Exception {
        // Arrange
        Long nonexistentId = 999L;
        doThrow(new FailedAlertNotFoundException(nonexistentId))
                .when(useCase).execute(nonexistentId);

        // Act & Assert
        mockMvc.perform(post("/failed-alerts/{id}/reprocess", nonexistentId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Alerta com falha não encontrado"))
                .andExpect(jsonPath("$.message").value("Alerta com falha ID 999 não encontrado"));
    }

    @Test
    void shouldReturn500OnUnexpectedError() throws Exception {
        doThrow(new RuntimeException("Erro inesperado")).when(useCase).execute(2L);

        mockMvc.perform(post("/failed-alerts/2/reprocess"))
                .andExpect(status().isInternalServerError());

    }
}
