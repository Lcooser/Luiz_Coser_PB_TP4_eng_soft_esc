package br.edu.esc.tp4.shared.domain;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LinkedResourceIdsTest {

    @Test
    void removeDuplicadosEMantemOrdenacao() {
        LinkedResourceIds ids = LinkedResourceIds.of(List.of(
                EntityId.of(3L),
                EntityId.of(1L),
                EntityId.of(3L)
        ));

        assertEquals(List.of(EntityId.of(1L), EntityId.of(3L)), ids.asList());
    }

    @Test
    void removeRecursoSelecionado() {
        LinkedResourceIds ids = LinkedResourceIds.of(List.of(EntityId.of(1L), EntityId.of(2L)));

        LinkedResourceIds atualizado = ids.without(EntityId.of(2L));

        assertEquals(List.of(EntityId.of(1L)), atualizado.asList());
    }

    @Test
    void vazioPermaneceVazio() {
        assertTrue(LinkedResourceIds.empty().isEmpty());
    }
}
