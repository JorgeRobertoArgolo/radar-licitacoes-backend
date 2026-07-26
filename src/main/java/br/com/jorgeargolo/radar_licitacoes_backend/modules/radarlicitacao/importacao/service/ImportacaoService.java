package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.importacao.service;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.model.HistoricoCompra;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.repository.IHistoricoCompraRepository;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.importacao.dto.response.ImportacaoResumoResponseDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.model.Produto;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.repository.IProdutoRepository;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ImportacaoService implements IImportacaoService {

    private final IProdutoRepository produtoRepository;
    private final IHistoricoCompraRepository historicoCompraRepository;

    @Override
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "produtos", allEntries = true),
            @CacheEvict(value = "historicoPorProduto", allEntries = true),
            @CacheEvict(value = "dashboardKpis", allEntries = true)
    })
    public ImportacaoResumoResponseDTO importarCsv(final MultipartFile file) {
        log.info("Iniciando importação de arquivo CSV: {}", file.getOriginalFilename());

        if (file.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "O arquivo CSV não pode estar vazio");
        }

        int produtosCriados = 0;
        int historicosImportados = 0;
        List<HistoricoCompra> historicosParaSalvar = new ArrayList<>();
        Map<String, Produto> produtosCache = new HashMap<>(); // Para evitar queries repetidas na mesma importação

        try (CSVReader reader = new CSVReaderBuilder(new InputStreamReader(file.getInputStream()))
                .withCSVParser(new CSVParserBuilder().withSeparator(';').build())
                .withSkipLines(1) // Pula o cabeçalho
                .build()) {

            String[] linha;
            int linhaNum = 1;

            while ((linha = reader.readNext()) != null) {
                linhaNum++;
                
                // Validação básica da linha
                if (linha.length < 6) {
                    log.warn("Linha {} ignorada pois não possui 6 colunas", linhaNum);
                    continue; 
                }

                String nomeProduto = linha[0].trim();
                String unidadeMedida = linha[1].trim();
                LocalDate dataCompra = LocalDate.parse(linha[2].trim());
                Integer quantidade = Integer.parseInt(linha[3].trim());
                BigDecimal precoUnitario = new BigDecimal(linha[4].trim());
                String fornecedor = linha[5].trim();

                Produto produto = produtosCache.get(nomeProduto.toLowerCase());
                
                if (produto == null) {
                    Optional<Produto> produtoExistente = produtoRepository.findByNomeIgnoreCase(nomeProduto);
                    
                    if (produtoExistente.isPresent()) {
                        produto = produtoExistente.get();
                    } else {
                        produto = new Produto();
                        produto.setNome(nomeProduto);
                        produto.setUnidadeMedida(unidadeMedida);
                        produto = produtoRepository.save(produto);
                        produtosCriados++;
                    }
                    produtosCache.put(nomeProduto.toLowerCase(), produto);
                }

                HistoricoCompra historico = new HistoricoCompra();
                historico.setProduto(produto);
                historico.setDataCompra(dataCompra);
                historico.setQuantidade(quantidade);
                historico.setPrecoUnitario(precoUnitario);
                historico.setFornecedor(fornecedor);

                historicosParaSalvar.add(historico);
                historicosImportados++;
            }

            if (!historicosParaSalvar.isEmpty()) {
                historicoCompraRepository.saveAll(historicosParaSalvar);
            }

            log.info("Importação concluída. Produtos criados: {}. Históricos importados: {}", produtosCriados, historicosImportados);
            
            return new ImportacaoResumoResponseDTO(
                    produtosCriados, 
                    historicosImportados, 
                    "Importação concluída com sucesso."
            );

        } catch (Exception e) {
            log.error("Erro ao processar arquivo CSV", e);
            throw new ResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, "Erro ao ler e processar o arquivo CSV. Verifique se o formato está correto (separador ; e dados válidos). Detalhe: " + e.getMessage());
        }
    }
}
