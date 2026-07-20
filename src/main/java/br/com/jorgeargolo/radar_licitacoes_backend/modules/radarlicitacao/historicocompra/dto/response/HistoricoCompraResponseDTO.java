package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO de resposta representando um registro de histórico de compra.")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record HistoricoCompraResponseDTO(

        @Schema(description = "ID do registro de histórico", example = "1")
        @JsonProperty("id")
        Long id,

        @Schema(description = "ID do produto associado", example = "1")
        @JsonProperty("produtoId")
        Long produtoId,

        @Schema(description = "Nome do produto associado", example = "Amoxicilina 500mg")
        @JsonProperty("produtoNome")
        String produtoNome,

        @Schema(description = "Data em que a compra foi realizada", example = "2025-10-12")
        @JsonProperty("dataCompra")
        LocalDate dataCompra,

        @Schema(description = "Quantidade comprada", example = "1000")
        @JsonProperty("quantidade")
        Integer quantidade,

        @Schema(description = "Preço unitário do produto", example = "0.85")
        @JsonProperty("precoUnitario")
        BigDecimal precoUnitario,

        @Schema(description = "Nome do fornecedor da compra", example = "FarmaVida LTDA")
        @JsonProperty("fornecedor")
        String fornecedor
) {
}
