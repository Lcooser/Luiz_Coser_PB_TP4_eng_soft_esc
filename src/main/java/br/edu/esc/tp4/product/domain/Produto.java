package br.edu.esc.tp4.product.domain;

import br.edu.esc.tp4.shared.domain.EntityId;
import br.edu.esc.tp4.shared.domain.IdentifiedEntity;
import br.edu.esc.tp4.shared.domain.LinkedResourceIds;
import br.edu.esc.tp4.shared.domain.MoneyValue;
import br.edu.esc.tp4.shared.domain.StockQuantity;

import java.util.Objects;

public final class Produto implements IdentifiedEntity {

    private final EntityId id;
    private final String nome;
    private final String descricao;
    private final MoneyValue preco;
    private final StockQuantity quantidadeEstoque;
    private final LinkedResourceIds recursoIds;

    private Produto(
            EntityId id,
            String nome,
            String descricao,
            MoneyValue preco,
            StockQuantity quantidadeEstoque,
            LinkedResourceIds recursoIds
    ) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.quantidadeEstoque = quantidadeEstoque;
        this.recursoIds = recursoIds;
    }

    public static Produto criar(
            EntityId id,
            String nome,
            String descricao,
            MoneyValue preco,
            StockQuantity quantidadeEstoque,
            LinkedResourceIds recursoIds
    ) {
        validarNome(nome);
        validarDescricao(descricao);
        return new Produto(
                id,
                nome.trim(),
                descricao.trim(),
                preco,
                quantidadeEstoque,
                recursoIds == null ? LinkedResourceIds.empty() : recursoIds
        );
    }

    private static void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("Nome do produto e obrigatorio.");
        }
    }

    private static void validarDescricao(String descricao) {
        if (descricao == null || descricao.isBlank()) {
            throw new IllegalArgumentException("Descricao do produto e obrigatoria.");
        }
    }

    public Produto removerVinculo(EntityId recursoId) {
        return new Produto(id, nome, descricao, preco, quantidadeEstoque, recursoIds.without(recursoId));
    }

    @Override
    public EntityId getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public MoneyValue getPreco() {
        return preco;
    }

    public StockQuantity getQuantidadeEstoque() {
        return quantidadeEstoque;
    }

    public LinkedResourceIds getRecursoIds() {
        return recursoIds;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Produto produto)) {
            return false;
        }
        return Objects.equals(id, produto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
