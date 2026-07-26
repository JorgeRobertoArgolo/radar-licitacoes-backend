package br.com.jorgeargolo.radar_licitacoes_backend.infraestructure.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "DTO padrão de resposta para erros da API")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponseDTO(

        @Schema(description = "Código HTTP do erro", example = "404")
        @JsonProperty("status")
        Integer status,

        @Schema(description = "Tipo do erro HTTP", example = "Not Found")
        @JsonProperty("erro")
        String erro,

        @Schema(description = "Mensagem descritiva do erro", example = "Produto não encontrado com o ID: 99")
        @JsonProperty("mensagem")
        String mensagem,

        @Schema(description = "Timestamp do momento em que o erro ocorreu", example = "2026-07-26T14:00:00")
        @JsonProperty("timestamp")
        LocalDateTime timestamp,

        @Schema(description = "Mapa de erros de validação por campo (apenas para erros 422)")
        @JsonProperty("errosCampo")
        Map<String, String> errosCampo
) {
}
