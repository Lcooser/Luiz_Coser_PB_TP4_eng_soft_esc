package br.edu.esc.tp4.integration.application;

import java.util.List;

public record ProdutoResumo(
        Long id,
        String nome,
        String descricao,
        String preco,
        Integer quantidadeEstoque,
        List<String> recursos
) {
}
