package br.edu.esc.tp4.resource.domain;

import br.edu.esc.tp4.shared.domain.EntityId;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecursoTest {

    @Test
    void rejeitaTituloVazio() {
        assertThrows(IllegalArgumentException.class, () -> Recurso.criar(
                EntityId.of(1L),
                " ",
                "Descricao",
                SituacaoRecurso.ATIVO
        ));
    }

    @Test
    void marcaRecursoAtivoCorretamente() {
        Recurso recurso = Recurso.criar(EntityId.of(1L), "Manual", "Descricao", SituacaoRecurso.ATIVO);
        assertTrue(recurso.isAtivo());
    }

    @Test
    void rejeitaDescricaoMuitoLonga() {
        String descricao = "x".repeat(2001);

        assertThrows(IllegalArgumentException.class, () -> Recurso.criar(
                EntityId.of(2L),
                "Manual",
                descricao,
                SituacaoRecurso.ATIVO
        ));
    }

    @Test
    void igualdadeConsideraSomenteId() {
        Recurso primeiro = Recurso.criar(EntityId.of(3L), "Manual A", "Descricao", SituacaoRecurso.ATIVO);
        Recurso segundo = Recurso.criar(EntityId.of(3L), "Manual B", "Descricao", SituacaoRecurso.INATIVO);

        assertEquals(primeiro, segundo);
        assertEquals(Objects.hash(EntityId.of(3L)), primeiro.hashCode());
    }
}
