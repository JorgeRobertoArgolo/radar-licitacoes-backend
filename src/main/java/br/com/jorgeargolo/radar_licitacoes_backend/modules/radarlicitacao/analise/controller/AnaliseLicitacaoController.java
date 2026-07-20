package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.analise.controller;

import br.com.jorgeargolo.radar_licitacoes_backend.infraestructure.util.ResultError;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.analise.dto.request.AnalisePrecoRequestDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.analise.dto.response.AnalisePrecoResponseDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.analise.service.IAnalisePrecoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/radar-licitacao/analise-licitacoes")
@RequiredArgsConstructor
@Tag(name = "Análise de Licitação", description = "Endpoints para análise estatística e detecção de sobrepreço")
public class AnaliseLicitacaoController {

    private final IAnalisePrecoService analisePrecoService;

    /**
     * Realiza a análise estatística de uma nova proposta de preço para verificar 
     * o risco de superfaturamento com base no histórico de compras.
     *
     * @param id          ‘ID’ do produto que está sendo analisado
     * @param requestDTO  DTO contendo o preço proposto a ser validado
     * @param result      BindingResult para validação dos dados de entrada
     * @return ResponseEntity<?> com o resultado detalhado da análise (escore-Z, alertas, etc.)
     */
    @Operation(summary = "Analisa se o preço proposto em uma licitação contém risco de superfaturamento")
    @PostMapping("/{id}/analisar")
    public ResponseEntity<?> analisarProposta(
            @PathVariable Long id,
            @Valid @RequestBody AnalisePrecoRequestDTO requestDTO,
            BindingResult result) {
        
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ResultError.getResultErrors(result));
        }

        AnalisePrecoResponseDTO response = analisePrecoService.analisarProposta(id, requestDTO.precoProposto());
        return ResponseEntity.ok(response);
    }
}
