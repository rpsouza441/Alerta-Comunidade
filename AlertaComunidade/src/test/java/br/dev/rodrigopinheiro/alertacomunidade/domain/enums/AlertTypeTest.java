package br.dev.rodrigopinheiro.alertacomunidade.domain.enums;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertTypeTest {

    @Test
    void testAllEnumValuesArePresent() {
        assertNotNull(AlertType.valueOf("FIRE"));
        assertNotNull(AlertType.valueOf("FLOOD"));
        assertNotNull(AlertType.valueOf("CRIME"));
        assertNotNull(AlertType.valueOf("WEATHER"));
        assertNotNull(AlertType.valueOf("MEDICAL"));
    }

    @Test
    void testEnumToString() {
        assertEquals("FIRE", AlertType.FIRE.toString());
        assertEquals("MEDICAL", AlertType.MEDICAL.toString());
    }

    @Test
    void testEnumValuesLength() {
        assertEquals(5, AlertType.values().length);
    }
}