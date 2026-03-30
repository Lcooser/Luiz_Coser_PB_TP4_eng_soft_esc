package br.edu.esc.tp4.web;

import br.edu.esc.tp4.integration.application.CatalogoIntegradoService;
import br.edu.esc.tp4.integration.application.OpcaoRecurso;
import br.edu.esc.tp4.integration.application.ProdutoResumo;
import br.edu.esc.tp4.product.domain.Produto;
import br.edu.esc.tp4.product.domain.ProdutoNaoEncontradoException;
import br.edu.esc.tp4.shared.domain.EntityId;
import br.edu.esc.tp4.shared.domain.LinkedResourceIds;
import br.edu.esc.tp4.shared.domain.MoneyValue;
import br.edu.esc.tp4.shared.domain.StockQuantity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ProdutoController.class)
class ProdutoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CatalogoIntegradoService catalogoIntegradoService;

    @Test
    void listaProdutosComSucesso() throws Exception {
        when(catalogoIntegradoService.listarProdutos()).thenReturn(List.of(
                new ProdutoResumo(1L, "Produto", "Descricao", "10.00", 1, List.of("Manual"))
        ));

        mvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(view().name("produtos/listagem"))
                .andExpect(model().attributeExists("produtos"));
    }

    @Test
    void abreFormularioNovoComRecursosDisponiveis() throws Exception {
        when(catalogoIntegradoService.listarOpcoesDeRecursos()).thenReturn(List.of(
                new OpcaoRecurso(1L, "Manual", true)
        ));

        mvc.perform(get("/produtos/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("produtos/formulario"))
                .andExpect(model().attributeExists("produto"))
                .andExpect(model().attributeExists("recursosDisponiveis"));
    }

    @Test
    void salvaProdutoValidoRedireciona() throws Exception {
        when(catalogoIntegradoService.produtoExiste(EntityId.of(1L))).thenReturn(false);

        mvc.perform(post("/produtos")
                        .param("id", "1")
                        .param("nome", "Produto")
                        .param("descricao", "Descricao")
                        .param("preco", "10.00")
                        .param("quantidadeEstoque", "5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));

        verify(catalogoIntegradoService).salvarProduto(any());
    }

    @Test
    void rejeitaFormularioComIdInvalido() throws Exception {
        when(catalogoIntegradoService.listarOpcoesDeRecursos()).thenReturn(List.of());

        mvc.perform(post("/produtos")
                        .param("id", "abc")
                        .param("nome", "Produto")
                        .param("descricao", "Descricao")
                        .param("preco", "10.00")
                        .param("quantidadeEstoque", "5"))
                .andExpect(status().isOk())
                .andExpect(view().name("produtos/formulario"))
                .andExpect(model().attributeExists("erro"));

        verify(catalogoIntegradoService, never()).salvarProduto(any());
    }

    @Test
    void editarInexistenteRedireciona() throws Exception {
        when(catalogoIntegradoService.buscarProduto(EntityId.of(99L))).thenReturn(Optional.empty());

        mvc.perform(get("/produtos/editar/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));
    }

    @Test
    void editarExistenteRetornaFormulario() throws Exception {
        when(catalogoIntegradoService.buscarProduto(EntityId.of(2L))).thenReturn(Optional.of(Produto.criar(
                EntityId.of(2L),
                "Produto",
                "Descricao",
                MoneyValue.of(new BigDecimal("10.00")),
                StockQuantity.of(2),
                LinkedResourceIds.of(List.of(EntityId.of(1L)))
        )));
        when(catalogoIntegradoService.listarOpcoesDeRecursos()).thenReturn(List.of(
                new OpcaoRecurso(1L, "Manual", true)
        ));

        mvc.perform(get("/produtos/editar/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("produtos/formulario"))
                .andExpect(model().attributeExists("produto"))
                .andExpect(model().attributeExists("recursosDisponiveis"));
    }

    @Test
    void removerComSucessoRedireciona() throws Exception {
        mvc.perform(post("/produtos/remover/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));

        verify(catalogoIntegradoService).removerProduto(EntityId.of(1L));
    }

    @Test
    void removerInexistenteRedirecionaComErro() throws Exception {
        doThrow(new ProdutoNaoEncontradoException(EntityId.of(5L)))
                .when(catalogoIntegradoService).removerProduto(EntityId.of(5L));

        mvc.perform(post("/produtos/remover/5"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));
    }

    @Test
    void rejeitaPrecoInvalido() throws Exception {
        when(catalogoIntegradoService.listarOpcoesDeRecursos()).thenReturn(List.of());

        mvc.perform(post("/produtos")
                        .param("id", "1")
                        .param("nome", "Produto")
                        .param("descricao", "Descricao")
                        .param("preco", "abc")
                        .param("quantidadeEstoque", "5"))
                .andExpect(status().isOk())
                .andExpect(view().name("produtos/formulario"))
                .andExpect(model().attributeExists("erro"));
    }
}
