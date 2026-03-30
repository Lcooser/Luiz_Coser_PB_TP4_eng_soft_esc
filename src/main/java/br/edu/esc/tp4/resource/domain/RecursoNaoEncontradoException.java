package br.edu.esc.tp4.resource.domain;

import br.edu.esc.tp4.shared.domain.EntityId;

public class RecursoNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RecursoNaoEncontradoException(EntityId id) {
        super("Recurso nao encontrado: " + id);
    }
}
