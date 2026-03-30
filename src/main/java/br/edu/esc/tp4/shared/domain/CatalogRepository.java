package br.edu.esc.tp4.shared.domain;

import java.util.List;
import java.util.Optional;

public interface CatalogRepository<T extends IdentifiedEntity> {

    void save(T entity);

    Optional<T> findById(EntityId id);

    List<T> findAll();

    void delete(EntityId id);

    boolean existsById(EntityId id);
}
