package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.analise.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta com o resultado da análise de sobrepreço")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record AnalisePrecoResponseDTO(

        @Schema(description = "Informa se há risco de superfaturamento", example = "true")
        @JsonProperty("riscoSuperfaturamento")
        Boolean riscoSuperfaturamento,

        @Schema(description = "Mensagem explicativa do resultado da análise", example = "Risco de Superfaturamento: Preço muito acima do histórico")
        @JsonProperty("mensagem")
        String mensagem,

        @Schema(description = "Média de preços históricos do produto", example = "1.25")
        @JsonProperty("mediaHistorica")
        Double mediaHistorica,

        @Schema(description = "Desvio padrão dos preços históricos", example = "0.10")
        @JsonProperty("desvioPadrao")
        Double desvioPadrao,

        @Schema(description = "Escore-Z calculado (se houver amostragem)", example = "2.5")
        @JsonProperty("escoreZ")
        Double escoreZ,

        @Schema(description = "Probabilidade acumulada calculada pelo commons-math3 (0 a 1)", example = "0.98")
        @JsonProperty("probabilidade")
        Double probabilidade,

        @Schema(description = "Quantidade de amostras utilizadas no cálculo", example = "15")
        @JsonProperty("quantidadeAmostras")
        Long quantidadeAmostras
) {
}
