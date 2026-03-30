package br.edu.esc.tp4.shared.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

class StockQuantityTest {

    @Test
    void rejeitaValorNulo() {
        assertThrows(IllegalArgumentException.class, () -> StockQuantity.of(null));
    }

    @Test
    void rejeitaValorNegativo() {
        assertThrows(IllegalArgumentException.class, () -> StockQuantity.of(-1));
    }
}
