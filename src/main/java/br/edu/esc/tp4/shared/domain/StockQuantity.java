package br.edu.esc.tp4.shared.domain;

public record StockQuantity(Integer value) {

    public StockQuantity {
        if (value == null) {
            throw new IllegalArgumentException("Quantidade em estoque e obrigatoria.");
        }
        if (value < 0) {
            throw new IllegalArgumentException("Quantidade em estoque deve ser maior ou igual a zero.");
        }
    }

    public static StockQuantity of(Integer value) {
        return new StockQuantity(value);
    }
}
