package br.com.jorgeargolo.radar_licitacoes_backend.infraestructure.handler;

import br.com.jorgeargolo.radar_licitacoes_backend.infraestructure.dto.ApiErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;

/**
 * Handler global de exceções da API.
 * Intercepta exceções lançadas pelos controllers e services,
 * padronizando as respostas de erro para o cliente.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Trata exceções do tipo {@link ResponseStatusException}.
     * Captura os erros 404, 500 e demais status lançados via {@code ResponseStatusException} nos services.
     *
     * @param ex A exceção lançada.
     * @return ResponseEntity com o DTO padronizado de erro.
     */
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<?> handleResponseStatusException(final ResponseStatusException ex) {
        log.warn("ResponseStatusException capturada: {} - {}", ex.getStatusCode(), ex.getReason());

        ApiErrorResponseDTO errorResponse = new ApiErrorResponseDTO(
                ex.getStatusCode().value(),
                HttpStatus.valueOf(ex.getStatusCode().value()).getReasonPhrase(),
                ex.getReason(),
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    /**
     * Trata exceções genéricas não previstas.
     * Garante que stack traces não sejam expostas ao cliente.
     *
     * @param ex A exceção genérica lançada.
     * @return ResponseEntity com erro 500 padronizado.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGenericException(final Exception ex) {
        log.error("Erro inesperado na API: {}", ex.getMessage(), ex);

        ApiErrorResponseDTO errorResponse = new ApiErrorResponseDTO(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                "Ocorreu um erro interno no servidor. Tente novamente mais tarde.",
                LocalDateTime.now(),
                null
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }
}
