package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.importacao.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "DTO de resposta com o resumo da operação de importação do CSV")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ImportacaoResumoResponseDTO(
        
        @Schema(description = "Quantidade de novos produtos cadastrados", example = "5")
        @JsonProperty("produtosCadastrados")
        int produtosCadastrados,
        
        @Schema(description = "Quantidade de registros de histórico de compras importados com sucesso", example = "150")
        @JsonProperty("historicosImportados")
        int historicosImportados,
        
        @Schema(description = "Mensagem geral sobre a operação", example = "Importação concluída com sucesso.")
        @JsonProperty("mensagem")
        String mensagem
) {
}
