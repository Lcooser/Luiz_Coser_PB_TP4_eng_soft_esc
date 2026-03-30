package br.edu.esc.tp4.integration.application;

import br.edu.esc.tp4.shared.domain.EntityId;
import br.edu.esc.tp4.shared.domain.LinkedResourceIds;
import br.edu.esc.tp4.shared.domain.MoneyValue;
import br.edu.esc.tp4.shared.domain.StockQuantity;

public record ProdutoCommand(
        EntityId id,
        String nome,
        String descricao,
        MoneyValue preco,
        StockQuantity quantidadeEstoque,
        LinkedResourceIds recursoIds
) {
}
