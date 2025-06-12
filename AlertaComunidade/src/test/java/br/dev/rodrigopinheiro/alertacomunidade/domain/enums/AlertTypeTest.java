package br.dev.rodrigopinheiro.alertacomunidade.domain.enums;


import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
class AlertTypeTest {


    @Test
    void shouldContainAllExpectedEnumValues() {
        assertThat(AlertType.values()).containsExactlyInAnyOrder(
                AlertType.FIRE,
                AlertType.FLOOD,
                AlertType.CRIME,
                AlertType.WEATHER,
                AlertType.MEDICAL,
                AlertType.OTHER
        );
    }

    @Test
    void shouldHaveExpectedNames() {
        assertThat(AlertType.FIRE.name()).isEqualTo("FIRE");
        assertThat(AlertType.MEDICAL.name()).isEqualTo("MEDICAL");
    }

    @Test
    void shouldHaveExactlySixEnumConstants() {
        assertThat(AlertType.values()).hasSize(6);
    }
}
