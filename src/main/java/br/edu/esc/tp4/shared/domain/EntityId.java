package br.edu.esc.tp4.shared.domain;

public record EntityId(Long value) implements Comparable<EntityId> {

    public EntityId {
        if (value == null) {
            throw new IllegalArgumentException("ID e obrigatorio.");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("ID deve ser positivo.");
        }
    }

    public static EntityId of(Long value) {
        return new EntityId(value);
    }

    @Override
    public int compareTo(EntityId other) {
        return value.compareTo(other.value);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
