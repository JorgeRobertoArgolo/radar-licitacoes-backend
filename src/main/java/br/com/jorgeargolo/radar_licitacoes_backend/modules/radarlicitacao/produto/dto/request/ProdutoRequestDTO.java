package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "DTO de requisição para cadastro de produto")
public record ProdutoRequestDTO(
        @NotBlank(message = "O nome do produto é obrigatório.")
        @Schema(description = "Nome descritivo do produto", example = "Amoxicilina 500mg")
        @JsonProperty("nome")
        String nome,

        @NotBlank(message = "A unidade de medida é obrigatória.")
        @Schema(description = "Unidade de medida (ex: Unidade, Caixa, Comprimido, Litro)", example = "Comprimido")
        @JsonProperty("unidadeMedida")
        String unidadeMedida
) {}
