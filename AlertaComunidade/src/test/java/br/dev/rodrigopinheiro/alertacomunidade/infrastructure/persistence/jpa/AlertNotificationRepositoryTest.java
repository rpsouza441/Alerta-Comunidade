package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.persistence.jpa;

import br.dev.rodrigopinheiro.alertacomunidade.AlertaComunidadeApplication;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;

import br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence.jpa.SpringDataAlertRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ContextConfiguration(classes = AlertaComunidadeApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ActiveProfiles("test")
class AlertNotificationRepositoryTest {


    @Autowired
    private SpringDataAlertRepository repository;

    @Test
    @DisplayName("Deve persistir alerta com sucesso")
    void shouldPersistAlert() {
        AlertNotification alert = createValidAlert("Teste de alerta", "INMET");

        AlertNotification saved = repository.save(alert);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getMessage()).isEqualTo("Teste de alerta");
    }


    @Test
    @DisplayName("Deve falhar ao persistir alerta com campos obrigatórios ausentes")
    void shouldFailWhenMissingFields() {
        AlertNotification alert = new AlertNotification(); // vazio

        // Esperado: exceção de constraint (pode ser javax ou jakarta dependendo da versão)
        org.junit.jupiter.api.Assertions.assertThrows(Exception.class, () -> {
            repository.saveAndFlush(alert);
        });
    }
    private static AlertNotification createValidAlert(String message, String origin) {
        AlertNotification alert = new AlertNotification();
        alert.setMessage(message);
        alert.setOrigin(origin);
        alert.setAlertType(AlertType.FIRE);
        alert.setStatus(AlertStatus.RECEIVED);
        alert.setCreatedAt(LocalDateTime.now());
        return alert;
    }
}
