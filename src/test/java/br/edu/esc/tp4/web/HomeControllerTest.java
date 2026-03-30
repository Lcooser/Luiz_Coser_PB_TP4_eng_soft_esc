package br.edu.esc.tp4.web;

import br.edu.esc.tp4.integration.application.CatalogoIntegradoService;
import br.edu.esc.tp4.integration.application.PainelResumo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CatalogoIntegradoService catalogoIntegradoService;

    @Test
    void carregaPainelInicial() throws Exception {
        when(catalogoIntegradoService.carregarPainel()).thenReturn(new PainelResumo(0, 0, 0));
        when(catalogoIntegradoService.listarProdutos()).thenReturn(List.of());
        when(catalogoIntegradoService.listarRecursos()).thenReturn(List.of());

        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("painel"))
                .andExpect(model().attributeExists("produtos"))
                .andExpect(model().attributeExists("recursos"));
    }
}
