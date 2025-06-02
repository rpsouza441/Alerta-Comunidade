package br.dev.rodrigopinheiro.alertacomunidade.application.mapper;

import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertStatus;
import br.dev.rodrigopinheiro.alertacomunidade.domain.enums.AlertType;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.AlertNotification;
import br.dev.rodrigopinheiro.alertacomunidade.domain.model.FailedAlertNotification;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

class FailedAlertMapperTest {

    @Test
    void testFromAlertNotificationAndErrorMessage() {
        AlertNotification alert = new AlertNotification();
        alert.setId(42L);
        alert.setMessage("Falha teste");
        alert.setOrigin("INMET");
        alert.setAlertType(AlertType.FIRE);
        alert.setStatus(AlertStatus.SENT_TO_QUEUE);
        alert.setCreatedAt(LocalDateTime.of(2025, 6, 1, 10, 0));

        FailedAlertNotification failed = FailedAlertMapper.from(alert, "Erro simulado");

        assertNotNull(failed);
        assertEquals("Falha teste", failed.getMessage());
        assertEquals("INMET", failed.getOrigin());
        assertEquals(AlertType.FIRE, failed.getAlertType());
        assertEquals(AlertStatus.FAILED, failed.getStatus());
        assertEquals("Erro simulado", failed.getErrorMessage());
        assertEquals(alert.getCreatedAt(), failed.getCreatedAt());
    }

    @Test
    void testToAlertNotificationFromFailed() {
        FailedAlertNotification failed = new FailedAlertNotification();
        failed.setOriginalId(42L);
        failed.setMessage("Alerta falho");
        failed.setOrigin("DEFESA_CIVIL");
        failed.setAlertType(AlertType.CRIME);
        failed.setStatus(AlertStatus.FAILED);
        failed.setCreatedAt(LocalDateTime.of(2025, 6, 2, 12, 0));

        AlertNotification alert = FailedAlertMapper.toAlertNotification(failed);

        assertNotNull(alert);
        assertEquals(42L, alert.getId());
        assertEquals("Alerta falho", alert.getMessage());
        assertEquals("DEFESA_CIVIL", alert.getOrigin());
        assertEquals(AlertType.CRIME, alert.getAlertType());
        assertEquals(AlertStatus.RECEIVED, alert.getStatus()); // Reset status
        assertEquals(failed.getCreatedAt(), alert.getCreatedAt());
    }
}
