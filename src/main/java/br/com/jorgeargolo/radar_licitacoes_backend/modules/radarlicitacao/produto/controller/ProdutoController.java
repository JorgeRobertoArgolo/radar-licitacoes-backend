package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.controller;

import br.com.jorgeargolo.radar_licitacoes_backend.infraestructure.util.ResultError;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.dto.request.ProdutoRequestDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.dto.response.ProdutoResponseDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.service.IProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

/**
 * Controller responsável por gerenciar os endpoints de produtos.
 */
@RestController
@RequestMapping("/api/v1/radar-licitacao/produtos")
@RequiredArgsConstructor
@Tag(name = "Produto", description = "Endpoints para gerenciamento do catálogo de produtos")
public class ProdutoController {

    private final IProdutoService produtoService;

    /**
     * Endpoint para salvar um novo produto no catálogo.
     *
     * @param dto Dados do produto a ser salvo.
     * @param result Resultado da validação do DTO.
     * @return DTO com o produto salvo ou os erros de validação.
     */
    @PostMapping
    @Operation(summary = "Salvar produto", description = "Cadastra um novo produto no catálogo")
    public ResponseEntity<?> salvarProduto(@Valid @RequestBody ProdutoRequestDTO dto, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ResultError.getResultErrors(result));
        }

        ProdutoResponseDTO savedDto = produtoService.salvarProduto(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedDto);
    }

    /**
     * Endpoint para listar todos os produtos com paginação.
     *
     * @param nome Filtro opcional pelo nome do produto.
     * @param pageable Configurações da paginação.
     * @return Página de produtos.
     */
    @GetMapping
    @Operation(summary = "Listar produtos", description = "Retorna uma lista paginada de todos os produtos do catálogo, com filtro opcional por nome")
    public ResponseEntity<Page<?>> listarProdutos(@RequestParam(required = false) String nome, @PageableDefault(size = 10) Pageable pageable) {
        Page<ProdutoResponseDTO> page = produtoService.listarProdutos(nome, pageable);
        return ResponseEntity.ok()
                .body(page);
    }

    /**
     * Endpoint para buscar um produto pelo ‘ID’.
     *
     * @param id ‘ID’ numérico do produto.
     * @return DTO contendo as informações do produto ou erro se não encontrado.
     */
    @GetMapping("/{id}")
    @Operation(summary = "Buscar produto por ID", description = "Busca e retorna um produto específico do catálogo")
    public ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        ProdutoResponseDTO dto = produtoService.buscarPorId(id);
        return ResponseEntity.ok()
                .body(dto);
    }
}
