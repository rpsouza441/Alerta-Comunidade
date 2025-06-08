package br.dev.rodrigopinheiro.alertacomunidade.domain.enums;

import br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertStatus;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AlertStatusTest {

    @Test
    void shouldContainAllExpectedEnumValues() {
        br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertStatus[] values = br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertStatus.values();

        assertThat(values).containsExactlyInAnyOrder(
                br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertStatus.RECEIVED,
                br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertStatus.SENT_TO_QUEUE,
                br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertStatus.DELIVERED,
                br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertStatus.FAILED,
                br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertStatus.REPROCESSED
        );
    }

    @Test
    void shouldMatchEnumNames() {
        assertThat(br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertStatus.RECEIVED.name()).isEqualTo("RECEIVED");
        assertThat(br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertStatus.FAILED.name()).isEqualTo("FAILED");
    }

    @Test
    void shouldHaveExactlyFiveEnumConstants() {
        assertThat(AlertStatus.values()).hasSize(5);
    }
}
