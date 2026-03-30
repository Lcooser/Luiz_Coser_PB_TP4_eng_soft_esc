package br.edu.esc.tp4.integration.application;

import br.edu.esc.tp4.product.infrastructure.ProdutoRepositoryImpl;
import br.edu.esc.tp4.resource.domain.SituacaoRecurso;
import br.edu.esc.tp4.resource.infrastructure.RecursoRepositoryImpl;
import br.edu.esc.tp4.shared.domain.EntityId;
import br.edu.esc.tp4.shared.domain.LinkedResourceIds;
import br.edu.esc.tp4.shared.domain.MoneyValue;
import br.edu.esc.tp4.shared.domain.StockQuantity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CatalogoIntegradoServiceTest {

    private CatalogoIntegradoService service;

    @BeforeEach
    void setUp() {
        service = new CatalogoIntegradoService(new ProdutoRepositoryImpl(), new RecursoRepositoryImpl());
    }

    @Test
    void impedeVinculoComRecursoInexistente() {
        ProdutoCommand command = new ProdutoCommand(
                EntityId.of(1L),
                "Produto",
                "Descricao",
                MoneyValue.of(new BigDecimal("10.00")),
                StockQuantity.of(2),
                LinkedResourceIds.of(List.of(EntityId.of(99L)))
        );

        assertThrows(IllegalArgumentException.class, () -> service.salvarProduto(command));
    }

    @Test
    void removerRecursoAtualizaProdutosVinculados() {
        service.salvarRecurso(new RecursoCommand(EntityId.of(1L), "Manual", "Descricao", SituacaoRecurso.ATIVO));
        service.salvarProduto(new ProdutoCommand(
                EntityId.of(2L),
                "Produto",
                "Descricao",
                MoneyValue.of(new BigDecimal("20.00")),
                StockQuantity.of(5),
                LinkedResourceIds.of(List.of(EntityId.of(1L)))
        ));

        service.removerRecurso(EntityId.of(1L));

        assertTrue(service.buscarProduto(EntityId.of(2L)).orElseThrow().getRecursoIds().isEmpty());
    }

    @Test
    void calculaPainelComTotaisIntegrados() {
        service.salvarRecurso(new RecursoCommand(EntityId.of(10L), "Treinamento", "Descricao", SituacaoRecurso.ATIVO));
        service.salvarProduto(new ProdutoCommand(
                EntityId.of(20L),
                "Kit",
                "Descricao",
                MoneyValue.of(new BigDecimal("15.00")),
                StockQuantity.of(1),
                LinkedResourceIds.of(List.of(EntityId.of(10L)))
        ));

        PainelResumo painel = service.carregarPainel();

        assertEquals(1, painel.totalProdutos());
        assertEquals(1, painel.totalRecursos());
        assertEquals(1, painel.totalVinculos());
    }

    @Test
    void listaResumoDeProdutosComTitulosDosRecursos() {
        service.salvarRecurso(new RecursoCommand(EntityId.of(30L), "Video", "Descricao", SituacaoRecurso.ATIVO));
        service.salvarProduto(new ProdutoCommand(
                EntityId.of(31L),
                "Kit",
                "Descricao",
                MoneyValue.of(new BigDecimal("12.00")),
                StockQuantity.of(3),
                LinkedResourceIds.of(List.of(EntityId.of(30L)))
        ));

        ProdutoResumo resumo = service.listarProdutos().get(0);

        assertEquals(List.of("Video"), resumo.recursos());
    }

    @Test
    void listaResumoDeRecursosComProdutosVinculados() {
        service.salvarRecurso(new RecursoCommand(EntityId.of(40L), "PDF", "Descricao", SituacaoRecurso.ATIVO));
        service.salvarProduto(new ProdutoCommand(
                EntityId.of(41L),
                "Produto X",
                "Descricao",
                MoneyValue.of(new BigDecimal("7.00")),
                StockQuantity.of(1),
                LinkedResourceIds.of(List.of(EntityId.of(40L)))
        ));

        RecursoResumo resumo = service.listarRecursos().get(0);

        assertEquals(1, resumo.totalProdutos());
        assertEquals(List.of("Produto X"), resumo.produtos());
    }
}
