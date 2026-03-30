package br.edu.esc.tp4.shared.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EntityIdTest {

    @Test
    void rejeitaIdNulo() {
        assertThrows(IllegalArgumentException.class, () -> EntityId.of(null));
    }

    @Test
    void rejeitaIdNaoPositivo() {
        assertThrows(IllegalArgumentException.class, () -> EntityId.of(0L));
    }

    @Test
    void representaValorComoTexto() {
        assertEquals("7", EntityId.of(7L).toString());
    }
}
