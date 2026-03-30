package br.edu.esc.tp4.web;

import br.edu.esc.tp4.integration.application.CatalogoIntegradoService;
import br.edu.esc.tp4.integration.application.RecursoCommand;
import br.edu.esc.tp4.resource.domain.Recurso;
import br.edu.esc.tp4.resource.domain.RecursoNaoEncontradoException;
import br.edu.esc.tp4.resource.domain.SituacaoRecurso;
import br.edu.esc.tp4.shared.domain.EntityId;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/recursos")
public class RecursoController {

    private final CatalogoIntegradoService catalogoIntegradoService;

    public RecursoController(CatalogoIntegradoService catalogoIntegradoService) {
        this.catalogoIntegradoService = catalogoIntegradoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("recursos", catalogoIntegradoService.listarRecursos());
        return "recursos/listagem";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        model.addAttribute("recurso", new RecursoForm());
        return "recursos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return catalogoIntegradoService.buscarRecurso(EntityId.of(id))
                .map(recurso -> {
                    model.addAttribute("recurso", RecursoForm.from(recurso));
                    return "recursos/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("erro", "Recurso nao encontrado.");
                    return "redirect:/recursos";
                });
    }

    @PostMapping
    public String salvar(@ModelAttribute RecursoForm form, Model model, RedirectAttributes redirectAttributes) {
        try {
            EntityId id = parseId(form.getId());
            boolean existia = catalogoIntegradoService.recursoExiste(id);
            catalogoIntegradoService.salvarRecurso(new RecursoCommand(
                    id,
                    form.getTitulo(),
                    form.getDescricao(),
                    parseSituacao(form.getSituacao())
            ));
            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    existia ? "Recurso atualizado com sucesso." : "Recurso cadastrado com sucesso."
            );
            return "redirect:/recursos";
        } catch (IllegalArgumentException error) {
            model.addAttribute("erro", error.getMessage());
            model.addAttribute("recurso", form);
            return "recursos/formulario";
        }
    }

    @PostMapping("/remover/{id}")
    public String remover(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            catalogoIntegradoService.removerRecurso(EntityId.of(id));
            redirectAttributes.addFlashAttribute("sucesso", "Recurso removido com sucesso.");
        } catch (RecursoNaoEncontradoException error) {
            redirectAttributes.addFlashAttribute("erro", "Recurso nao encontrado.");
        } catch (IllegalArgumentException error) {
            redirectAttributes.addFlashAttribute("erro", error.getMessage());
        }
        return "redirect:/recursos";
    }

    private EntityId parseId(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("ID e obrigatorio.");
        }
        try {
            return EntityId.of(Long.parseLong(raw.trim()));
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("ID invalido.");
        }
    }

    private SituacaoRecurso parseSituacao(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Situacao do recurso e obrigatoria.");
        }
        try {
            return SituacaoRecurso.valueOf(raw.trim().toUpperCase());
        } catch (IllegalArgumentException error) {
            throw new IllegalArgumentException("Situacao do recurso invalida.");
        }
    }

    public static final class RecursoForm {
        private String id;
        private String titulo;
        private String descricao;
        private String situacao = SituacaoRecurso.ATIVO.name();

        static RecursoForm from(Recurso recurso) {
            RecursoForm form = new RecursoForm();
            form.setId(recurso.getId().toString());
            form.setTitulo(recurso.getTitulo());
            form.setDescricao(recurso.getDescricao());
            form.setSituacao(recurso.getSituacao().name());
            return form;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitulo() {
            return titulo;
        }

        public void setTitulo(String titulo) {
            this.titulo = titulo;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public String getSituacao() {
            return situacao;
        }

        public void setSituacao(String situacao) {
            this.situacao = situacao;
        }
    }
}
