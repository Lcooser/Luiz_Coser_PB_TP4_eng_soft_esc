package br.edu.esc.tp4.shared.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;

public record MoneyValue(BigDecimal value) {

    public MoneyValue {
        if (value == null) {
            throw new IllegalArgumentException("Preco e obrigatorio.");
        }
        if (value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Preco deve ser maior ou igual a zero.");
        }
        value = value.setScale(2, RoundingMode.HALF_UP);
    }

    public static MoneyValue of(BigDecimal value) {
        return new MoneyValue(value);
    }
}
