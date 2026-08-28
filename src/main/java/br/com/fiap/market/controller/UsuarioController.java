package br.com.fiap.market.controller;

import br.com.fiap.market.dto.UsuarioCadastroDTO;
import br.com.fiap.market.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping("/cadastro")
    public String cadastro(Authentication authentication) {

        boolean autenticado =
                authentication != null
                        && authentication.isAuthenticated()
                        && !(authentication instanceof AnonymousAuthenticationToken);
        if (autenticado) {
            return "redirect:/itens";
        }

        return "cadastro";
    }

    @PostMapping("/cadastro")
    public String cadastrarUsuario(UsuarioCadastroDTO dto,
                                   Model model) {
        try {

            usuarioService.cadastrarUsuario(dto);
            return "redirect:/login?cadastro";
        } catch (IllegalArgumentException e) {

            model.addAttribute("erro", e.getMessage());
            model.addAttribute("usuario", dto);
            return "cadastro";
        }
    }
}
