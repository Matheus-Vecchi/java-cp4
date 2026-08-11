package br.com.fiap.market.controller;

import br.com.fiap.market.dto.ItemRequestDTO;
import br.com.fiap.market.dto.ItemResponseDTO;
import br.com.fiap.market.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController
@RequestMapping("/mercado")
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @PostMapping
    public ResponseEntity<ItemResponseDTO> cadastrarItem(@RequestBody
                                                             ItemRequestDTO dto) {
        ItemResponseDTO item = itemService.cadastrarItem(dto);
        adicionarLinks(item);
        URI location = linkTo(
                methodOn(ItemController.class)
                        .buscarItemPorId(item.getId())
        ).toUri();
        return ResponseEntity
                .created(location)
                .body(item);
    }

    @GetMapping
    public ResponseEntity<CollectionModel<ItemResponseDTO>> listarItens() {
        List<ItemResponseDTO> itens = itemService.listarItens();
        itens.forEach(this::adicionarLinks);
        CollectionModel<ItemResponseDTO> collection = CollectionModel.of(
                itens,
                linkTo(methodOn(ItemController.class)
                        .listarItens())
                        .withSelfRel()
        );

        return ResponseEntity.ok(collection);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> buscarItemPorId(@PathVariable
                                                               Long id) {
        ItemResponseDTO item = itemService.buscarItemPorId(id);
        adicionarLinks(item);
        return ResponseEntity.ok(item);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> atualizarItem(@PathVariable
                                                               Long id,
                                                               @RequestBody ItemRequestDTO dto) {
        ItemResponseDTO item = itemService.atualizarItem(id, dto);
        adicionarLinks(item);
        return ResponseEntity.ok(item);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ItemResponseDTO> atualizarParcial(@PathVariable
                                                               Long id,
                                                               @RequestBody ItemRequestDTO dto) {
        ItemResponseDTO item = itemService.atualizarParcial(id, dto);
        adicionarLinks(item);
        return ResponseEntity.ok(item);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirItem(@PathVariable Long id) {
        itemService.excluirItem(id);
        return ResponseEntity.noContent().build();
    }

    // agora eu vou adc o link de hateoas
    private void adicionarLinks(ItemResponseDTO item) {
        item.add(
                linkTo(methodOn(ItemController.class)
                        .buscarItemPorId(item.getId()))
                        .withSelfRel()
        );

        item.add(
                linkTo(methodOn(ItemController.class)
                        .listarItens())
                        .withRel("mercado")
        );
    }
}
