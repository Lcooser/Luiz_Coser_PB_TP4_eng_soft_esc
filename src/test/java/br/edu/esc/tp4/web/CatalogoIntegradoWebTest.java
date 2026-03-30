package br.edu.esc.tp4.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class CatalogoIntegradoWebTest {

    @Autowired
    private MockMvc mvc;

    @Test
    void removerRecursoRefleteNaListagemDeProdutos() throws Exception {
        mvc.perform(post("/recursos")
                        .param("id", "701")
                        .param("titulo", "Manual Integrado")
                        .param("descricao", "Arquivo de apoio")
                        .param("situacao", "ATIVO"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recursos"));

        mvc.perform(post("/produtos")
                        .param("id", "702")
                        .param("nome", "Kit Integrado")
                        .param("descricao", "Produto vinculado")
                        .param("preco", "49.90")
                        .param("quantidadeEstoque", "8")
                        .param("recursoIds", "701"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/produtos"));

        mvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Manual Integrado")));

        mvc.perform(get("/recursos"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Kit Integrado")));

        mvc.perform(post("/recursos/remover/701"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recursos"));

        mvc.perform(get("/produtos"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("Manual Integrado"))));
    }
}
