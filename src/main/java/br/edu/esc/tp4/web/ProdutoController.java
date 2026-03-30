package br.edu.esc.tp4.web;

import br.edu.esc.tp4.integration.application.CatalogoIntegradoService;
import br.edu.esc.tp4.integration.application.ProdutoCommand;
import br.edu.esc.tp4.product.domain.Produto;
import br.edu.esc.tp4.product.domain.ProdutoNaoEncontradoException;
import br.edu.esc.tp4.shared.domain.EntityId;
import br.edu.esc.tp4.shared.domain.LinkedResourceIds;
import br.edu.esc.tp4.shared.domain.MoneyValue;
import br.edu.esc.tp4.shared.domain.StockQuantity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/produtos")
public class ProdutoController {

    private final CatalogoIntegradoService catalogoIntegradoService;

    public ProdutoController(CatalogoIntegradoService catalogoIntegradoService) {
        this.catalogoIntegradoService = catalogoIntegradoService;
    }

    @GetMapping
    public String listar(Model model) {
        model.addAttribute("produtos", catalogoIntegradoService.listarProdutos());
        return "produtos/listagem";
    }

    @GetMapping("/novo")
    public String novo(Model model) {
        preencherFormulario(model, new ProdutoForm());
        return "produtos/formulario";
    }

    @GetMapping("/editar/{id}")
    public String editar(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return catalogoIntegradoService.buscarProduto(EntityId.of(id))
                .map(produto -> {
                    preencherFormulario(model, ProdutoForm.from(produto));
                    return "produtos/formulario";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("erro", "Produto nao encontrado.");
                    return "redirect:/produtos";
                });
    }

    @PostMapping
    public String salvar(@ModelAttribute ProdutoForm form, Model model, RedirectAttributes redirectAttributes) {
        try {
            EntityId id = parseId(form.getId());
            boolean existia = catalogoIntegradoService.produtoExiste(id);
            catalogoIntegradoService.salvarProduto(new ProdutoCommand(
                    id,
                    form.getNome(),
                    form.getDescricao(),
                    parsePreco(form.getPreco()),
                    parseQuantidade(form.getQuantidadeEstoque()),
                    parseRecursos(form.getRecursoIds())
            ));
            redirectAttributes.addFlashAttribute(
                    "sucesso",
                    existia ? "Produto atualizado com sucesso." : "Produto cadastrado com sucesso."
            );
            return "redirect:/produtos";
        } catch (IllegalArgumentException error) {
            model.addAttribute("erro", error.getMessage());
            preencherFormulario(model, form);
            return "produtos/formulario";
        }
    }

    @PostMapping("/remover/{id}")
    public String remover(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            catalogoIntegradoService.removerProduto(EntityId.of(id));
            redirectAttributes.addFlashAttribute("sucesso", "Produto removido com sucesso.");
        } catch (ProdutoNaoEncontradoException error) {
            redirectAttributes.addFlashAttribute("erro", "Produto nao encontrado.");
        } catch (IllegalArgumentException error) {
            redirectAttributes.addFlashAttribute("erro", error.getMessage());
        }
        return "redirect:/produtos";
    }

    private void preencherFormulario(Model model, ProdutoForm form) {
        model.addAttribute("produto", form);
        model.addAttribute("recursosDisponiveis", catalogoIntegradoService.listarOpcoesDeRecursos());
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

    private MoneyValue parsePreco(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Preco e obrigatorio.");
        }
        try {
            return MoneyValue.of(new BigDecimal(raw.trim().replace(',', '.')));
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Preco invalido.");
        }
    }

    private StockQuantity parseQuantidade(String raw) {
        if (raw == null || raw.isBlank()) {
            throw new IllegalArgumentException("Quantidade em estoque e obrigatoria.");
        }
        try {
            return StockQuantity.of(Integer.parseInt(raw.trim()));
        } catch (NumberFormatException error) {
            throw new IllegalArgumentException("Quantidade em estoque invalida.");
        }
    }

    private LinkedResourceIds parseRecursos(List<String> rawIds) {
        if (rawIds == null || rawIds.isEmpty()) {
            return LinkedResourceIds.empty();
        }
        List<EntityId> ids = new ArrayList<>();
        for (String rawId : rawIds) {
            if (rawId == null || rawId.isBlank()) {
                continue;
            }
            ids.add(parseId(rawId));
        }
        return LinkedResourceIds.of(ids);
    }

    public static final class ProdutoForm {
        private String id;
        private String nome;
        private String descricao;
        private String preco;
        private String quantidadeEstoque;
        private List<String> recursoIds = new ArrayList<>();

        static ProdutoForm from(Produto produto) {
            ProdutoForm form = new ProdutoForm();
            form.setId(produto.getId().toString());
            form.setNome(produto.getNome());
            form.setDescricao(produto.getDescricao());
            form.setPreco(produto.getPreco().value().toPlainString());
            form.setQuantidadeEstoque(String.valueOf(produto.getQuantidadeEstoque().value()));
            form.setRecursoIds(produto.getRecursoIds().asList().stream().map(EntityId::toString).toList());
            return form;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public String getDescricao() {
            return descricao;
        }

        public void setDescricao(String descricao) {
            this.descricao = descricao;
        }

        public String getPreco() {
            return preco;
        }

        public void setPreco(String preco) {
            this.preco = preco;
        }

        public String getQuantidadeEstoque() {
            return quantidadeEstoque;
        }

        public void setQuantidadeEstoque(String quantidadeEstoque) {
            this.quantidadeEstoque = quantidadeEstoque;
        }

        public List<String> getRecursoIds() {
            return recursoIds;
        }

        public void setRecursoIds(List<String> recursoIds) {
            this.recursoIds = recursoIds == null ? new ArrayList<>() : new ArrayList<>(recursoIds);
        }
    }
}
