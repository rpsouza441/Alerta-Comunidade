package br.dev.rodrigopinheiro.alertacomunidade.domain.enums;

import br.dev.rodrigopinheiro.alertacomunidade.application.domain.enums.AlertStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AlertStatusTest {

    @Test
    void shouldContainAllExpectedEnumValues() {
        AlertStatus[] values = AlertStatus.values();

        assertThat(values).containsExactlyInAnyOrder(
                AlertStatus.RECEIVED,
                AlertStatus.SENT_TO_QUEUE,
                AlertStatus.DELIVERED,
                AlertStatus.FAILED,
                AlertStatus.REPROCESSED
        );
    }

    @Test
    void shouldMatchEnumNames() {
        assertThat(AlertStatus.RECEIVED.name()).isEqualTo("RECEIVED");
        assertThat(AlertStatus.FAILED.name()).isEqualTo("FAILED");
    }

    @Test
    void shouldHaveExactlyFiveEnumConstants() {
        assertThat(AlertStatus.values()).hasSize(5);
    }
}
