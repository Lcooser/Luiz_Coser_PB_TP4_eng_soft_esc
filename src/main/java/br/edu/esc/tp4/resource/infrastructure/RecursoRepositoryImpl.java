package br.edu.esc.tp4.resource.infrastructure;

import br.edu.esc.tp4.resource.domain.Recurso;
import br.edu.esc.tp4.resource.domain.RecursoNaoEncontradoException;
import br.edu.esc.tp4.resource.domain.RecursoRepository;
import br.edu.esc.tp4.shared.domain.EntityId;
import br.edu.esc.tp4.shared.infrastructure.InMemoryStore;

import java.util.List;
import java.util.Optional;

public final class RecursoRepositoryImpl implements RecursoRepository {

    private final InMemoryStore<Recurso> store = new InMemoryStore<>();

    @Override
    public void save(Recurso entity) {
        store.save(entity);
    }

    @Override
    public Optional<Recurso> findById(EntityId id) {
        return store.findById(id);
    }

    @Override
    public List<Recurso> findAll() {
        return store.findAll();
    }

    @Override
    public void delete(EntityId id) {
        if (!store.delete(id)) {
            throw new RecursoNaoEncontradoException(id);
        }
    }

    @Override
    public boolean existsById(EntityId id) {
        return store.existsById(id);
    }
}
