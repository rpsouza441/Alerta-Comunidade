package br.dev.rodrigopinheiro.alertacomunidade.application.mapper;

import br.dev.rodrigopinheiro.alertacomunidade.common.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.common.domain.model.FailedAlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.common.mapper.FailedAlertMapper;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FailedAlertMapperTest {


    @Test
    void shouldMapFromAlertNotificationCorrectly() {
        AlertNotification alert = new AlertNotification();
        alert.setId(42L);
        alert.setMessage("Falha teste");
        alert.setOrigin("INMET");
        alert.setAlertType(AlertType.FIRE);
        alert.setStatus(AlertStatus.SENT_TO_QUEUE);
        alert.setCreatedAt(LocalDateTime.of(2025, 6, 1, 10, 0));

        FailedAlertNotification failed = FailedAlertMapper.from(alert, "Erro simulado");

        assertThat(failed).isNotNull();
        assertThat(failed.getOriginalId()).isEqualTo(42L);
        assertThat(failed.getMessage()).isEqualTo("Falha teste");
        assertThat(failed.getOrigin()).isEqualTo("INMET");
        assertThat(failed.getAlertType()).isEqualTo(AlertType.FIRE);
        assertThat(failed.getStatus()).isEqualTo(AlertStatus.FAILED);
        assertThat(failed.getErrorMessage()).isEqualTo("Erro simulado");
        assertThat(failed.getCreatedAt()).isEqualTo(alert.getCreatedAt());
    }

    @Test
    void shouldMapToAlertNotificationCorrectly() {
        FailedAlertNotification failed = new FailedAlertNotification();
        failed.setOriginalId(42L);
        failed.setMessage("Alerta falho");
        failed.setOrigin("DEFESA_CIVIL");
        failed.setAlertType(AlertType.CRIME);
        failed.setStatus(AlertStatus.FAILED);
        failed.setCreatedAt(LocalDateTime.of(2025, 6, 2, 12, 0));

        AlertNotification alert = FailedAlertMapper.toAlertNotification(failed);

        assertThat(alert).isNotNull();
        assertThat(alert.getId()).isEqualTo(42L);
        assertThat(alert.getMessage()).isEqualTo("Alerta falho");
        assertThat(alert.getOrigin()).isEqualTo("DEFESA_CIVIL");
        assertThat(alert.getAlertType()).isEqualTo(AlertType.CRIME);
        assertThat(alert.getStatus()).isEqualTo(AlertStatus.RECEIVED); // reset status
        assertThat(alert.getCreatedAt()).isEqualTo(failed.getCreatedAt());
    }

    @Test
    void shouldHandleNullOriginalIdGracefully() {
        FailedAlertNotification failed = new FailedAlertNotification();
        failed.setOriginalId(null); // importante
        failed.setMessage("Sem ID original");
        failed.setOrigin("DEFESA_CIVIL");
        failed.setAlertType(AlertType.FLOOD);
        failed.setStatus(AlertStatus.FAILED);
        failed.setCreatedAt(LocalDateTime.of(2025, 6, 3, 8, 30));

        AlertNotification alert = FailedAlertMapper.toAlertNotification(failed);

        assertThat(alert).isNotNull();
        assertThat(alert.getId()).isNull(); // não deve setar ID
        assertThat(alert.getMessage()).isEqualTo("Sem ID original");
        assertThat(alert.getAlertType()).isEqualTo(AlertType.FLOOD);
    }

    @Test
    void shouldThrowNullPointerIfAlertIsNull() {
        assertThrows(NullPointerException.class, () -> FailedAlertMapper.from(null, "erro"));
    }

    @Test
    void shouldThrowNullPointerIfFailedIsNull() {
        assertThrows(NullPointerException.class, () -> FailedAlertMapper.toAlertNotification(null));
    }

}
