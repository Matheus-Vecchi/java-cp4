package br.com.fiap.market.service;


import br.com.fiap.market.dto.ItemRequestDTO;
import br.com.fiap.market.dto.ItemResponseDTO;
import br.com.fiap.market.entity.Item;
import br.com.fiap.market.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    // aqui é cadastro, blzzz??
    public ItemResponseDTO cadastrarItem(ItemRequestDTO dto) {
        Item item = toEntity(dto);

        Item itemSalvo = itemRepository.save(item);
        return toResponseDTO(itemSalvo);
    }

    // aqui lista todos produtos
    public List<ItemResponseDTO> listarItens() {
        return itemRepository.findAll()
                .stream()
                .map(this::toResponseDTO)
                .toList();
    }

    // agora faz a busca por id
    public ItemResponseDTO buscarItemPorId(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado com o id: " + id));

        return toResponseDTO(item);
    }

    // atualizar o produto
    public ItemResponseDTO atualizarItem(Long id, ItemRequestDTO dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado com o id: " + id));

        item.setNome(dto.getNome());
        item.setTipo(dto.getTipo());
        item.setSetor(dto.getSetor());
        item.setTamanho(dto.getTamanho());
        item.setPreco(dto.getPreco());

        Item itemAtualizado = itemRepository.save(item);

        return toResponseDTO(itemAtualizado);
    }

    // atualizar por parte - PATCH
    public ItemResponseDTO atualizarParcial(Long id, ItemRequestDTO dto) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        if (dto.getNome() != null) {
            item.setNome(dto.getNome());
        }

        if (dto.getTipo() != null) {
            item.setTipo(dto.getTipo());
        }

        if (dto.getSetor() != null) {
            item.setSetor(dto.getSetor());
        }

        if (dto.getTamanho() != null) {
            item.setTamanho(dto.getTamanho());
        }

        if (dto.getPreco() != null) {
            item.setPreco(dto.getPreco());
        }

        Item itemAtualizado = itemRepository.save(item);

        return toResponseDTO(itemAtualizado);
    }

    // excluir o produto
    public void excluirItem(Long id) {
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        itemRepository.delete(item);
    }

    // esse é o requestDTO > entidades
    private Item toEntity(ItemRequestDTO dto) {
        Item item = new Item();

        item.setNome(dto.getNome());
        item.setTipo(dto.getTipo());
        item.setSetor(dto.getSetor());
        item.setTamanho(dto.getTamanho());
        item.setPreco(dto.getPreco());

        return item;
    }

    // agora entidades pra responseDTO
    private ItemResponseDTO toResponseDTO(Item item) {
        return new ItemResponseDTO(
                item.getId(),
                item.getNome(),
                item.getTipo(),
                item.getSetor(),
                item.getTamanho(),
                item.getPreco()
        );
    }
}
