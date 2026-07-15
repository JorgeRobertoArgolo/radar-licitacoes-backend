package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta representando um produto")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProdutoResponseDTO(
        @Schema(description = "Identificador único do produto", example = "1")
        @JsonProperty("id")
        Long id,

        @Schema(description = "Nome descritivo do produto", example = "Amoxicilina 500mg")
        @JsonProperty("nome")
        String nome,

        @Schema(description = "Unidade de medida do produto", example = "Comprimido")
        @JsonProperty("unidade_medida")
        String unidadeMedida
) {}
