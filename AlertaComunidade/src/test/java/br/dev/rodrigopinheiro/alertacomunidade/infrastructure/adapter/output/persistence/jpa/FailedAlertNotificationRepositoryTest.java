package br.dev.rodrigopinheiro.alertacomunidade.infrastructure.adapter.output.persistence.jpa;


import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
public class FailedAlertNotificationRepositoryTest {


    @Autowired
    private SpringDataFailedAlertRepository failedAlertRepository;

    @Test
    @DisplayName("Deve persistir FailedAlert com sucesso")
    public void shouldPersistFailedAlert() {
        FailedAlertNotification failedAlertNotification = createValidFailedAlert
                (
                        "Teste de alerta",
                        "INMET",
                        "Erro message"
                );
        FailedAlertNotification savedFailedAlertNotification = failedAlertRepository.save(failedAlertNotification);
        assertThat(savedFailedAlertNotification.getId()).isNotNull();
        assertThat(savedFailedAlertNotification.getMessage()).isNotNull();
    }

    @Test
    @DisplayName("Deve falhar ao persistir FailedAlert")
    void shouldFailWhenMissingFields() {
        FailedAlertNotification failedAlertNotification = new FailedAlertNotification();

        assertThatThrownBy(() -> failedAlertRepository.saveAndFlush(failedAlertNotification))
                .isInstanceOf(ConstraintViolationException.class);

    }


    private FailedAlertNotification createValidFailedAlert(String message, String origin, String error) {
        FailedAlertNotification failedAlertNotification = new FailedAlertNotification();
        failedAlertNotification.setMessage(message);
        failedAlertNotification.setOrigin(origin);
        failedAlertNotification.setCreatedAt(LocalDateTime.now());
        failedAlertNotification.setErrorMessage(error);
        failedAlertNotification.setFailedAt(LocalDateTime.now());
        failedAlertNotification.setAlertType(AlertType.WEATHER);
        failedAlertNotification.setStatus(AlertStatus.FAILED);
        return failedAlertNotification;

    }

}
