package br.dev.rodrigopinheiro.alertacomunidade.domain.enums;


import br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertType;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
class AlertTypeTest {


    @Test
    void shouldContainAllExpectedEnumValues() {
        assertThat(br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertType.values()).containsExactlyInAnyOrder(
                br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertType.FIRE,
                br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertType.FLOOD,
                br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertType.CRIME,
                br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertType.WEATHER,
                br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertType.MEDICAL,
                br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertType.OTHER
        );
    }

    @Test
    void shouldHaveExpectedNames() {
        assertThat(br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertType.FIRE.name()).isEqualTo("FIRE");
        assertThat(br.dev.rodrigopinheiro.alertacomunidade.common.enums.AlertType.MEDICAL.name()).isEqualTo("MEDICAL");
    }

    @Test
    void shouldHaveExactlySixEnumConstants() {
        assertThat(AlertType.values()).hasSize(6);
    }
}