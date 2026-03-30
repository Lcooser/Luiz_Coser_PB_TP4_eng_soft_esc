package br.edu.esc.tp4.product.infrastructure;

import br.edu.esc.tp4.product.domain.Produto;
import br.edu.esc.tp4.product.domain.ProdutoNaoEncontradoException;
import br.edu.esc.tp4.product.domain.ProdutoRepository;
import br.edu.esc.tp4.shared.domain.EntityId;
import br.edu.esc.tp4.shared.domain.LinkedResourceIds;
import br.edu.esc.tp4.shared.domain.MoneyValue;
import br.edu.esc.tp4.shared.domain.StockQuantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProdutoRepositoryImplTest {

    private ProdutoRepository repository;

    @BeforeEach
    void setUp() {
        repository = new ProdutoRepositoryImpl();
    }

    @Test
    void salvaEBuscaProduto() {
        Produto produto = Produto.criar(
                EntityId.of(1L),
                "Produto",
                "Descricao",
                MoneyValue.of(new BigDecimal("15.00")),
                StockQuantity.of(3),
                LinkedResourceIds.empty()
        );

        repository.save(produto);

        assertTrue(repository.findById(EntityId.of(1L)).isPresent());
    }

    @Test
    void removeInexistenteLancaExcecao() {
        assertThrows(ProdutoNaoEncontradoException.class, () -> repository.delete(EntityId.of(99L)));
    }

    @Test
    void informaExistenciaPorId() {
        repository.save(Produto.criar(
                EntityId.of(2L),
                "Outro",
                "Descricao",
                MoneyValue.of(new BigDecimal("5.00")),
                StockQuantity.of(1),
                LinkedResourceIds.empty()
        ));

        assertEquals(1, repository.findAll().size());
        assertTrue(repository.existsById(EntityId.of(2L)));
    }
}
