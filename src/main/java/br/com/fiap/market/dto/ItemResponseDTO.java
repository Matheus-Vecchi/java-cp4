package br.com.fiap.market.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
// o callSuper = true é pra dizer que o equals e hashcode vai levar em consideração a classe pai, que nesse caso é a RepresentationModel,
// que é do Spring HATEOAS, que é uma biblioteca que ajuda a criar links nos retornos das APIs REST.
public class ItemResponseDTO extends RepresentationModel<ItemResponseDTO> {
    // o mesmo contexto do outro que eu falei, esse aqui é pra devolver dados no get
    private Long id;
    private String nome;
    private String tipo;
    private String setor;
    private String tamanho;
    private BigDecimal preco;
}
