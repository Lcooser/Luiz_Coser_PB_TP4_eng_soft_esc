package br.edu.esc.tp4.shared.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MoneyValueTest {

    @Test
    void rejeitaValorNegativo() {
        assertThrows(IllegalArgumentException.class, () -> MoneyValue.of(new BigDecimal("-1.00")));
    }

    @Test
    void ajustaEscalaParaDuasCasas() {
        MoneyValue value = MoneyValue.of(new BigDecimal("10"));
        assertEquals(new BigDecimal("10.00"), value.value());
    }
}
