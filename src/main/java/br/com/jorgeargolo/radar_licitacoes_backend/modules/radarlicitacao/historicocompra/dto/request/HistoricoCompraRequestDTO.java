package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "DTO de requisição para registrar um novo histórico de compra.")
public record HistoricoCompraRequestDTO(

        @Schema(description = "ID do produto associado à compra", example = "1")
        @JsonProperty("produtoId")
        @NotNull(message = "O ID do produto é obrigatório")
        Long produtoId,

        @Schema(description = "Data em que a compra foi realizada", example = "2025-10-12")
        @JsonProperty("dataCompra")
        @NotNull(message = "A data da compra é obrigatória")
        @PastOrPresent(message = "A data da compra não pode estar no futuro")
        LocalDate dataCompra,

        @Schema(description = "Quantidade comprada", example = "1000")
        @JsonProperty("quantidade")
        @NotNull(message = "A quantidade é obrigatória")
        @Min(value = 1, message = "A quantidade deve ser pelo menos 1")
        Integer quantidade,

        @Schema(description = "Preço unitário do produto na compra", example = "0.85")
        @JsonProperty("precoUnitario")
        @NotNull(message = "O preço unitário é obrigatório")
        @DecimalMin(value = "0.01", message = "O preço unitário deve ser maior que zero")
        BigDecimal precoUnitario,

        @Schema(description = "Nome do fornecedor da compra", example = "FarmaVida LTDA")
        @JsonProperty("fornecedor")
        @NotBlank(message = "O nome do fornecedor é obrigatório")
        String fornecedor
) {
}
