package br.edu.esc.tp4.integration.application;

import br.edu.esc.tp4.resource.domain.SituacaoRecurso;
import br.edu.esc.tp4.shared.domain.EntityId;

public record RecursoCommand(
        EntityId id,
        String titulo,
        String descricao,
        SituacaoRecurso situacao
) {
}
