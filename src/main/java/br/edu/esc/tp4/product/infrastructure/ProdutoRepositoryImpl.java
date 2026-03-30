package br.edu.esc.tp4.product.infrastructure;

import br.edu.esc.tp4.product.domain.Produto;
import br.edu.esc.tp4.product.domain.ProdutoNaoEncontradoException;
import br.edu.esc.tp4.product.domain.ProdutoRepository;
import br.edu.esc.tp4.shared.domain.EntityId;
import br.edu.esc.tp4.shared.infrastructure.InMemoryStore;

import java.util.List;
import java.util.Optional;

public final class ProdutoRepositoryImpl implements ProdutoRepository {

    private final InMemoryStore<Produto> store = new InMemoryStore<>();

    @Override
    public void save(Produto entity) {
        store.save(entity);
    }

    @Override
    public Optional<Produto> findById(EntityId id) {
        return store.findById(id);
    }

    @Override
    public List<Produto> findAll() {
        return store.findAll();
    }

    @Override
    public void delete(EntityId id) {
        if (!store.delete(id)) {
            throw new ProdutoNaoEncontradoException(id);
        }
    }

    @Override
    public boolean existsById(EntityId id) {
        return store.existsById(id);
    }
}
