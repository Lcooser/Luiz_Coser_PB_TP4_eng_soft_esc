package br.edu.esc.tp4.resource.domain;

import br.edu.esc.tp4.shared.domain.EntityId;
import br.edu.esc.tp4.shared.domain.IdentifiedEntity;

import java.util.Objects;

public final class Recurso implements IdentifiedEntity {

    private static final int TITULO_MAX = 200;
    private static final int DESCRICAO_MAX = 2000;

    private final EntityId id;
    private final String titulo;
    private final String descricao;
    private final SituacaoRecurso situacao;

    private Recurso(EntityId id, String titulo, String descricao, SituacaoRecurso situacao) {
        this.id = id;
        this.titulo = titulo;
        this.descricao = descricao;
        this.situacao = situacao;
    }

    public static Recurso criar(EntityId id, String titulo, String descricao, SituacaoRecurso situacao) {
        validarTitulo(titulo);
        validarDescricao(descricao);
        if (situacao == null) {
            throw new IllegalArgumentException("Situacao do recurso e obrigatoria.");
        }
        return new Recurso(id, titulo.trim(), descricao, situacao);
    }

    private static void validarTitulo(String titulo) {
        if (titulo == null || titulo.isBlank()) {
            throw new IllegalArgumentException("Titulo do recurso e obrigatorio.");
        }
        if (titulo.trim().length() > TITULO_MAX) {
            throw new IllegalArgumentException("Titulo do recurso excede o limite permitido.");
        }
    }

    private static void validarDescricao(String descricao) {
        if (descricao == null) {
            throw new IllegalArgumentException("Descricao do recurso e obrigatoria.");
        }
        if (descricao.length() > DESCRICAO_MAX) {
            throw new IllegalArgumentException("Descricao do recurso excede o limite permitido.");
        }
    }

    @Override
    public EntityId getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public SituacaoRecurso getSituacao() {
        return situacao;
    }

    public boolean isAtivo() {
        return situacao == SituacaoRecurso.ATIVO;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Recurso recurso)) {
            return false;
        }
        return Objects.equals(id, recurso.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
