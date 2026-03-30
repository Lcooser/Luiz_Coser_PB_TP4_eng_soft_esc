package br.edu.esc.tp4.shared.domain;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public final class LinkedResourceIds {

    private final Set<EntityId> values;

    private LinkedResourceIds(Set<EntityId> values) {
        this.values = Set.copyOf(values);
    }

    public static LinkedResourceIds empty() {
        return new LinkedResourceIds(Set.of());
    }

    public static LinkedResourceIds of(Collection<EntityId> ids) {
        if (ids == null || ids.isEmpty()) {
            return empty();
        }
        return new LinkedResourceIds(new LinkedHashSet<>(ids));
    }

    public List<EntityId> asList() {
        return values.stream()
                .sorted()
                .toList();
    }

    public boolean contains(EntityId id) {
        return values.contains(id);
    }

    public LinkedResourceIds without(EntityId id) {
        if (id == null || !values.contains(id)) {
            return this;
        }
        return new LinkedResourceIds(values.stream()
                .filter(value -> !value.equals(id))
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new)));
    }

    public boolean isEmpty() {
        return values.isEmpty();
    }
}
