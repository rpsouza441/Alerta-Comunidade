package br.dev.rodrigopinheiro.alertacomunidade.domain.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AlertStatusTest {

    @Test
    void testAllEnumValuesArePresent() {
        assertNotNull(AlertStatus.valueOf("RECEIVED"));
        assertNotNull(AlertStatus.valueOf("SENT_TO_QUEUE"));
        assertNotNull(AlertStatus.valueOf("DELIVERED"));
        assertNotNull(AlertStatus.valueOf("FAILED"));
        assertNotNull(AlertStatus.valueOf("REPROCESSED"));
    }

    @Test
    void testEnumToString() {
        assertEquals("RECEIVED", AlertStatus.RECEIVED.toString());
        assertEquals("FAILED", AlertStatus.FAILED.toString());
    }

    @Test
    void testEnumValuesLength() {
        assertEquals(5, AlertStatus.values().length);
    }
}
