package br.edu.esc.tp4.product.domain;

import br.edu.esc.tp4.shared.domain.EntityId;

public class ProdutoNaoEncontradoException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ProdutoNaoEncontradoException(EntityId id) {
        super("Produto nao encontrado: " + id);
    }
}
