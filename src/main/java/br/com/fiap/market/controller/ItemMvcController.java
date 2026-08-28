package br.com.fiap.market.controller;


import br.com.fiap.market.dto.ItemRequestDTO;
import br.com.fiap.market.dto.ItemResponseDTO;
import br.com.fiap.market.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.AnonymousAuthenticationToken;

import java.util.List;



@Controller
@RequestMapping("/itens")
@RequiredArgsConstructor
public class ItemMvcController {

    private final ItemService itemService;

    @GetMapping
    public String listarItens(Model model, Authentication authentication) {
        boolean autenticado = false;
        if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
            autenticado = true;
        }
        List<ItemResponseDTO> itens = itemService.listarItens();
        model.addAttribute("itens", itens);
        model.addAttribute("autenticado", autenticado);
        return "itens/listar";
    }

    @GetMapping("/novo")
    public String novoItem() {
        return "itens/formulario";
    }

    @GetMapping("/{id}/editar")
    public String editarItem(@PathVariable Long id,
                             Model model) {
        ItemResponseDTO item = itemService.buscarItemPorId(id);
        model.addAttribute("item", item);
        return "itens/editar";
    }

    @PostMapping
    public String cadastrarItem(ItemRequestDTO dto) {
        itemService.cadastrarItem(dto);
        return "redirect:/itens";
    }

    @PutMapping("/{id}/editar")
    public String atualizarItem(@PathVariable Long id, ItemRequestDTO dto) {
        itemService.atualizarItem(id, dto);
        return "redirect:/itens";
    }

    @DeleteMapping("/{id}/excluir")
    public String excluirItem(@PathVariable Long id) {
        itemService.excluirItem(id);
        return "redirect:/itens";
    }
}
