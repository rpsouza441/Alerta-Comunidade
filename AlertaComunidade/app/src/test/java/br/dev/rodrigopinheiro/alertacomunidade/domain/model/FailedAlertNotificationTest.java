package br.dev.rodrigopinheiro.alertacomunidade.domain.model;

import br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class FailedAlertNotificationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    void shouldCreateValidFailedAlertNotification() {
        FailedAlertNotification failed = new FailedAlertNotification();
        failed.setMessage("Mensagem de erro");
        failed.setOrigin("INMET");
        failed.setOriginalId(10L);
        failed.setAlertType(AlertType.MEDICAL);
        failed.setStatus(AlertStatus.FAILED);
        failed.setCreatedAt(LocalDateTime.now());
        failed.setErrorMessage("Erro na publicação do alerta");
        failed.setFailedAt(LocalDateTime.now());

        Set<ConstraintViolation<FailedAlertNotification>> violations = validator.validate(failed);
        assertThat(violations).isEmpty();

    }

    @Test
    void shouldFailWhenRequiredFieldsAreMissing() {
        FailedAlertNotification failed = new FailedAlertNotification();

        Set<ConstraintViolation<FailedAlertNotification>> violations = validator.validate(failed);

        assertThat(violations).isNotEmpty();
        assertThat(violations)
                .extracting(v -> v.getPropertyPath().toString())
                .contains("message", "origin", "alertType", "status", "createdAt", "errorMessage", "failedAt");
    }
}
