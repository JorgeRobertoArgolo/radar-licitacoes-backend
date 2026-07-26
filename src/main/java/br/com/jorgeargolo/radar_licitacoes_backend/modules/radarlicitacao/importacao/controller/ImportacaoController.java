package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.importacao.controller;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.importacao.dto.response.ImportacaoResumoResponseDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.importacao.service.IImportacaoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/radar-licitacao/importacao")
@RequiredArgsConstructor
@Tag(name = "Importação", description = "Endpoints para importação em lote de dados via CSV")
public class ImportacaoController {

    private final IImportacaoService importacaoService;

    /**
     * Endpoint para upload e importação de arquivo CSV contendo dados de produtos e históricos.
     *
     * @param file Arquivo `.csv` recebido no formato multipart/form-data.
     * @return DTO contendo a quantidade de produtos cadastrados e históricos importados.
     */
    @PostMapping(value = "/csv", consumes = {"multipart/form-data"})
    @Operation(summary = "Importar CSV", description = "Importa uma planilha CSV, cadastrando novos produtos e carregando o histórico de compras")
    public ResponseEntity<?> importarCsv(@RequestParam("file") MultipartFile file) {
        ImportacaoResumoResponseDTO resumo = importacaoService.importarCsv(file);
        return ResponseEntity.status(HttpStatus.CREATED).body(resumo);
    }
}
