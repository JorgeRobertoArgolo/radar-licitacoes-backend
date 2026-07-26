package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.dashboard.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;

@Schema(description = "DTO de resposta com os indicadores consolidados (KPIs) do painel principal")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DashboardKpiResponseDTO(

        @Schema(description = "Total de produtos cadastrados no catálogo", example = "4")
        @JsonProperty("totalProdutosCadastrados")
        Long totalProdutosCadastrados,

        @Schema(description = "Total de registros de compras no histórico", example = "30")
        @JsonProperty("totalComprasRegistradas")
        Long totalComprasRegistradas,

        @Schema(description = "Quantidade de fornecedores distintos no histórico", example = "8")
        @JsonProperty("totalFornecedoresDistintos")
        Long totalFornecedoresDistintos,

        @Schema(description = "Valor total acumulado de compras (preço unitário × quantidade)", example = "125430.50")
        @JsonProperty("valorTotalCompras")
        BigDecimal valorTotalCompras
) {
}
