package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.controller;

import br.com.jorgeargolo.radar_licitacoes_backend.infraestructure.util.ResultError;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.dto.request.HistoricoCompraRequestDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.dto.response.HistoricoCompraResponseDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.service.IHistoricoCompraService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/v1/radar-licitacao/historico-compras")
@RequiredArgsConstructor
@Tag(name = "Histórico de Compras", description = "Endpoints para gerenciamento do histórico de compras")
public class HistoricoCompraController {

    private final IHistoricoCompraService historicoCompraService;

    /**
     * Salva um novo registro no histórico de compras.
     * Este endpoint retroalimenta o banco de dados que servirá como amostragem para a inteligência estatística.
     *
     * @param requestDTO DTO contendo os dados do histórico de compra a ser salvo
     * @param result     BindingResult para validação dos dados de entrada
     * @return ResponseEntity<?> com os dados do histórico salvo ou os erros de validação
     */
    @Operation(summary = "Salva um novo registro no histórico de compras")
    @PostMapping
    public ResponseEntity<?> salvarHistorico(
            @Valid @RequestBody HistoricoCompraRequestDTO requestDTO,
            BindingResult result) {
        
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ResultError.getResultErrors(result));
        }

        HistoricoCompraResponseDTO response = historicoCompraService.salvarHistorico(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Lista o histórico de compras vinculado a um produto específico de forma paginada,
     * com filtro opcional por quantidade para análise de economia de escala.
     *
     * @param produtoId  'ID' do produto na base de dados
     * @param quantidade Filtro por quantidade exata da compra (opcional)
     * @param pageable   Configurações de paginação
     * @return ResponseEntity<?> com a página contendo os históricos do produto
     */
    @Operation(summary = "Lista o histórico de compras de um produto específico", description = "Retorna uma lista paginada do histórico, com filtro opcional por quantidade")
    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<?> listarHistoricoPorProduto(
            @PathVariable Long produtoId,
            @RequestParam(required = false) Integer quantidade,
            @PageableDefault Pageable pageable) {
        
        Page<HistoricoCompraResponseDTO> response = historicoCompraService.listarHistoricoPorProduto(produtoId, quantidade, pageable);
        
        return ResponseEntity.ok()
                .cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS))
                .body(response);
    }
}
