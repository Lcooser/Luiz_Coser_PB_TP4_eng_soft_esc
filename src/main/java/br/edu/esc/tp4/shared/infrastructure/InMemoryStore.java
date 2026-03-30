package br.edu.esc.tp4.shared.infrastructure;

import br.edu.esc.tp4.shared.domain.EntityId;
import br.edu.esc.tp4.shared.domain.IdentifiedEntity;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class InMemoryStore<T extends IdentifiedEntity> {

    private final Map<EntityId, T> values = new ConcurrentHashMap<>();

    public void save(T entity) {
        if (entity == null) {
            throw new IllegalArgumentException("Entidade nao pode ser nula.");
        }
        values.put(entity.getId(), entity);
    }

    public Optional<T> findById(EntityId id) {
        if (id == null) {
            return Optional.empty();
        }
        return Optional.ofNullable(values.get(id));
    }

    public List<T> findAll() {
        return values.values().stream()
                .sorted(Comparator.comparing(IdentifiedEntity::getId))
                .toList();
    }

    public boolean delete(EntityId id) {
        if (id == null) {
            throw new IllegalArgumentException("ID e obrigatorio.");
        }
        return values.remove(id) != null;
    }

    public boolean existsById(EntityId id) {
        return id != null && values.containsKey(id);
    }
}
