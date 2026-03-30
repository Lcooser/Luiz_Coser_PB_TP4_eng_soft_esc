package br.edu.esc.tp4.product.domain;

import br.edu.esc.tp4.shared.domain.EntityId;
import br.edu.esc.tp4.shared.domain.LinkedResourceIds;
import br.edu.esc.tp4.shared.domain.MoneyValue;
import br.edu.esc.tp4.shared.domain.StockQuantity;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProdutoTest {

    @Test
    void rejeitaNomeVazio() {
        assertThrows(IllegalArgumentException.class, () -> Produto.criar(
                EntityId.of(1L),
                " ",
                "Descricao",
                MoneyValue.of(new BigDecimal("10.00")),
                StockQuantity.of(1),
                LinkedResourceIds.empty()
        ));
    }

    @Test
    void removeVinculoSemAlterarDemaisDados() {
        Produto produto = Produto.criar(
                EntityId.of(1L),
                "Produto",
                "Descricao",
                MoneyValue.of(new BigDecimal("10.00")),
                StockQuantity.of(5),
                LinkedResourceIds.of(List.of(EntityId.of(7L), EntityId.of(8L)))
        );

        Produto atualizado = produto.removerVinculo(EntityId.of(7L));

        assertEquals("Produto", atualizado.getNome());
        assertEquals(List.of(EntityId.of(8L)), atualizado.getRecursoIds().asList());
    }

    @Test
    void rejeitaDescricaoVazia() {
        assertThrows(IllegalArgumentException.class, () -> Produto.criar(
                EntityId.of(2L),
                "Produto",
                " ",
                MoneyValue.of(new BigDecimal("10.00")),
                StockQuantity.of(1),
                LinkedResourceIds.empty()
        ));
    }

    @Test
    void igualdadeConsideraSomenteId() {
        Produto primeiro = Produto.criar(
                EntityId.of(3L),
                "A",
                "Descricao A",
                MoneyValue.of(new BigDecimal("10.00")),
                StockQuantity.of(1),
                LinkedResourceIds.empty()
        );
        Produto segundo = Produto.criar(
                EntityId.of(3L),
                "B",
                "Descricao B",
                MoneyValue.of(new BigDecimal("20.00")),
                StockQuantity.of(2),
                LinkedResourceIds.empty()
        );

        assertEquals(primeiro, segundo);
        assertEquals(primeiro.hashCode(), segundo.hashCode());
        assertNotEquals(primeiro, null);
        assertTrue(primeiro.equals(primeiro));
    }
}
