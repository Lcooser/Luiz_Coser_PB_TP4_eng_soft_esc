package br.edu.esc.tp4.resource.infrastructure;

import br.edu.esc.tp4.resource.domain.Recurso;
import br.edu.esc.tp4.resource.domain.RecursoNaoEncontradoException;
import br.edu.esc.tp4.resource.domain.RecursoRepository;
import br.edu.esc.tp4.resource.domain.SituacaoRecurso;
import br.edu.esc.tp4.shared.domain.EntityId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RecursoRepositoryImplTest {

    private RecursoRepository repository;

    @BeforeEach
    void setUp() {
        repository = new RecursoRepositoryImpl();
    }

    @Test
    void salvaEBuscaRecurso() {
        repository.save(Recurso.criar(EntityId.of(1L), "Manual", "Descricao", SituacaoRecurso.ATIVO));

        assertTrue(repository.findById(EntityId.of(1L)).isPresent());
    }

    @Test
    void removeInexistenteLancaExcecao() {
        assertThrows(RecursoNaoEncontradoException.class, () -> repository.delete(EntityId.of(99L)));
    }

    @Test
    void listaRecursosOrdenados() {
        repository.save(Recurso.criar(EntityId.of(2L), "B", "Descricao", SituacaoRecurso.ATIVO));
        repository.save(Recurso.criar(EntityId.of(1L), "A", "Descricao", SituacaoRecurso.INATIVO));

        assertEquals(2, repository.findAll().size());
        assertEquals(EntityId.of(1L), repository.findAll().get(0).getId());
    }
}
