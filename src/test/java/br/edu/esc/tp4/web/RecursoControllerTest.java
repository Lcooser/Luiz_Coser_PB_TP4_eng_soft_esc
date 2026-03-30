package br.edu.esc.tp4.web;

import br.edu.esc.tp4.integration.application.CatalogoIntegradoService;
import br.edu.esc.tp4.integration.application.RecursoResumo;
import br.edu.esc.tp4.resource.domain.Recurso;
import br.edu.esc.tp4.resource.domain.RecursoNaoEncontradoException;
import br.edu.esc.tp4.resource.domain.SituacaoRecurso;
import br.edu.esc.tp4.shared.domain.EntityId;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

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

@WebMvcTest(RecursoController.class)
class RecursoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CatalogoIntegradoService catalogoIntegradoService;

    @Test
    void listaRecursosComSucesso() throws Exception {
        when(catalogoIntegradoService.listarRecursos()).thenReturn(List.of(
                new RecursoResumo(1L, "Manual", "Descricao", "Ativo", 0, List.of())
        ));

        mvc.perform(get("/recursos"))
                .andExpect(status().isOk())
                .andExpect(view().name("recursos/listagem"))
                .andExpect(model().attributeExists("recursos"));
    }

    @Test
    void salvaRecursoValidoRedireciona() throws Exception {
        when(catalogoIntegradoService.recursoExiste(EntityId.of(1L))).thenReturn(false);

        mvc.perform(post("/recursos")
                        .param("id", "1")
                        .param("titulo", "Manual")
                        .param("descricao", "Descricao")
                        .param("situacao", "ATIVO"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recursos"));

        verify(catalogoIntegradoService).salvarRecurso(any());
    }

    @Test
    void rejeitaRecursoComIdInvalido() throws Exception {
        mvc.perform(post("/recursos")
                        .param("id", "abc")
                        .param("titulo", "Manual")
                        .param("descricao", "Descricao")
                        .param("situacao", "ATIVO"))
                .andExpect(status().isOk())
                .andExpect(view().name("recursos/formulario"))
                .andExpect(model().attributeExists("erro"));

        verify(catalogoIntegradoService, never()).salvarRecurso(any());
    }

    @Test
    void editarInexistenteRedireciona() throws Exception {
        when(catalogoIntegradoService.buscarRecurso(EntityId.of(99L))).thenReturn(Optional.empty());

        mvc.perform(get("/recursos/editar/99"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recursos"));
    }

    @Test
    void novoRetornaFormulario() throws Exception {
        mvc.perform(get("/recursos/novo"))
                .andExpect(status().isOk())
                .andExpect(view().name("recursos/formulario"))
                .andExpect(model().attributeExists("recurso"));
    }

    @Test
    void editarExistenteRetornaFormulario() throws Exception {
        when(catalogoIntegradoService.buscarRecurso(EntityId.of(2L))).thenReturn(Optional.of(
                Recurso.criar(EntityId.of(2L), "Manual", "Descricao", SituacaoRecurso.ATIVO)
        ));

        mvc.perform(get("/recursos/editar/2"))
                .andExpect(status().isOk())
                .andExpect(view().name("recursos/formulario"))
                .andExpect(model().attributeExists("recurso"));
    }

    @Test
    void removerComSucessoRedireciona() throws Exception {
        mvc.perform(post("/recursos/remover/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recursos"));

        verify(catalogoIntegradoService).removerRecurso(EntityId.of(1L));
    }

    @Test
    void removerInexistenteRedireciona() throws Exception {
        doThrow(new RecursoNaoEncontradoException(EntityId.of(3L)))
                .when(catalogoIntegradoService).removerRecurso(EntityId.of(3L));

        mvc.perform(post("/recursos/remover/3"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recursos"));
    }

    @Test
    void rejeitaSituacaoInvalida() throws Exception {
        mvc.perform(post("/recursos")
                        .param("id", "1")
                        .param("titulo", "Manual")
                        .param("descricao", "Descricao")
                        .param("situacao", "zzz"))
                .andExpect(status().isOk())
                .andExpect(view().name("recursos/formulario"))
                .andExpect(model().attributeExists("erro"));
    }
}
