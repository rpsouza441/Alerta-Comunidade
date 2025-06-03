package br.dev.rodrigopinheiro.alertacomunidade.domain.model;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
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
public class AlertNotificationTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }
    @Test
    void shouldCreateValidAlertNotification() {
        long id = 10L;
        String message = "Message";
        String origin= "Origin";
        AlertType type= AlertType.MEDICAL;
        LocalDateTime now = LocalDateTime.now();
        AlertStatus status = AlertStatus.DELIVERED;
        AlertNotification alertNotification = new AlertNotification(
                id,
                message,
                origin,
                type,
                now,
                status
        );
        assertThat(alertNotification.getId()).isEqualTo(id);
        assertThat(alertNotification.getMessage()).isEqualTo(message);
        assertThat(alertNotification.getOrigin()).isEqualTo(origin);
        assertThat(alertNotification.getAlertType()).isEqualTo(type);
        assertThat(alertNotification.getCreatedAt()).isEqualTo(now);
        assertThat(alertNotification.getStatus()).isEqualTo(status);

    }


    @Test
    void shouldFailWhenRequiredFieldsAreMissing() {
        AlertNotification alert = new AlertNotification();

        Set<ConstraintViolation<AlertNotification>> violations = validator.validate(alert);

        assertThat(violations).isNotEmpty();
        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("message"));
    }
}
