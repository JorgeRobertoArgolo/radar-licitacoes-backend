package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.analise.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

@Schema(description = "DTO de requisição para solicitar a análise de preço de um produto")
public record AnalisePrecoRequestDTO(

        @Schema(description = "Preço proposto para o item na licitação", example = "1.50")
        @JsonProperty("precoProposto")
        @NotNull(message = "O preço proposto é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço proposto deve ser maior que zero")
        Double precoProposto
) {
}
