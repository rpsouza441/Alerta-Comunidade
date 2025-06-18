package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.input.rest;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.exception.ResourceNotFoundException;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAlertByIdInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.GetAllAlertsInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.domain.port.input.ProcessAlertInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.dto.AlertResponseDTO;
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


import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@WebMvcTest(AlertController.class)
@Import(AlertControllerTest.TestConfig.class)
class AlertControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    @Qualifier("processAlertInputPort")
    private ProcessAlertInputPort processAlertInputPort;

    @Autowired
    @Qualifier("getAllAlertsInputPort")
    private GetAllAlertsInputPort getAllAlertsInputPort;

    @Autowired
    @Qualifier("getAlertByIdInputPort")
    private GetAlertByIdInputPort getAlertByIdInputPort;

    @TestConfiguration
    static class TestConfig {
        @Bean(name = "processAlertInputPort")
        public ProcessAlertInputPort processAlertInputPort() {
            return mock(ProcessAlertInputPort.class);
        }

        @Bean(name = "getAllAlertsInputPort")
        public GetAllAlertsInputPort getAllAlertsInputPort() {
            return mock(GetAllAlertsInputPort.class);
        }
        @Bean(name = "getAlertByIdInputPort")
        public GetAlertByIdInputPort getAlertByIdInputPort() {
            return mock(GetAlertByIdInputPort.class);
        }

    }

    @Test
    void shouldReturn201WhenValidAlert() throws Exception {
        AlertResponseDTO dto = new AlertResponseDTO();
        dto.setId(1L);
        dto.setMessage("Chuva forte");
        dto.setOrigin("INMET");
        dto.setAlertType(AlertType.WEATHER);
        dto.setAlertStatus(AlertStatus.RECEIVED);
        dto.setCreatedAt(LocalDateTime.now());

        Page<AlertResponseDTO> page = new PageImpl<>(List.of(dto));
        when(getAllAlertsInputPort.getAllAlerts(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/alerts?page=0&size=10"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].message").value("Chuva forte"))
                .andExpect(jsonPath("$.content[0].origin").value("INMET"));


    }

    @Test
    void shouldReturnEmptyPageWhenNoAlertsExist() throws Exception {
        when(getAllAlertsInputPort.getAllAlerts(any(Pageable.class))).thenReturn(Page.empty());

        mockMvc.perform(get("/alerts"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content").isEmpty());
    }
    @Test
    void shouldReturn500WhenUnexpectedErrorOnList() throws Exception {
        when(getAllAlertsInputPort.getAllAlerts(any(Pageable.class)))
                .thenThrow(new RuntimeException("Erro inesperado"));

        mockMvc.perform(get("/alerts"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void shouldReturnAlertByIdSuccessfully() throws Exception {
        AlertResponseDTO dto = new AlertResponseDTO();
        dto.setId(1L);
        dto.setMessage("Fogo na floresta");
        dto.setOrigin("INMET");
        dto.setAlertType(AlertType.FIRE);
        dto.setAlertStatus(AlertStatus.RECEIVED);
        dto.setCreatedAt(LocalDateTime.now());

        when(getAlertByIdInputPort.getAlertById(1L)).thenReturn(dto);

        mockMvc.perform(get("/alerts/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.message").value("Fogo na floresta"))
                .andExpect(jsonPath("$.origin").value("INMET"));
    }

    @Test
    void shouldReturn404WhenAlertNotFound() throws Exception {
        when(getAlertByIdInputPort.getAlertById(99L))
                .thenThrow(new ResourceNotFoundException("Alerta com ID: 99 não encontrada"));

        mockMvc.perform(get("/alerts/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Alerta com ID: 99 não encontrada"));
    }

    @Test
    void shouldReturn500WhenUnexpectedErrorOnGetById() throws Exception {
        when(getAlertByIdInputPort.getAlertById(2L))
                .thenThrow(new RuntimeException("Falha interna simulada"));

        mockMvc.perform(get("/alerts/2"))
                .andExpect(status().isInternalServerError());
    }


}
