package br.edu.esc.tp4.web;

import br.edu.esc.tp4.integration.application.CatalogoIntegradoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final CatalogoIntegradoService catalogoIntegradoService;

    public HomeController(CatalogoIntegradoService catalogoIntegradoService) {
        this.catalogoIntegradoService = catalogoIntegradoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("painel", catalogoIntegradoService.carregarPainel());
        model.addAttribute("produtos", catalogoIntegradoService.listarProdutos().stream().limit(5).toList());
        model.addAttribute("recursos", catalogoIntegradoService.listarRecursos().stream().limit(5).toList());
        return "home";
    }
}
