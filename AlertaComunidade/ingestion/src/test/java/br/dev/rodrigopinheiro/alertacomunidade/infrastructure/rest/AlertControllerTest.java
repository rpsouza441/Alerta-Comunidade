package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.rest;

import br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.input.GetAlertByIdInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.input.GetAllAlertsInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.application.domain.port.input.ProcessAlertInputPort;
import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.infrastructure.rest.AlertController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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






}
