package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.service;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.dto.request.HistoricoCompraRequestDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.dto.response.HistoricoCompraResponseDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.model.HistoricoCompra;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.repository.IHistoricoCompraRepository;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.model.Produto;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.repository.IProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@Service
public class HistoricoCompraService implements IHistoricoCompraService {

    private final IHistoricoCompraRepository historicoCompraRepository;
    private final IProdutoRepository produtoRepository;

    @Override
    @Transactional
    @CacheEvict(value = "historicoPorProduto", allEntries = true)
    public HistoricoCompraResponseDTO salvarHistorico(final HistoricoCompraRequestDTO request) {
        log.info("Salvando novo histórico de compra para o produto de ID {}", request.produtoId());
        
        Produto produto = produtoRepository.findById(request.produtoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado com o ID: " + request.produtoId()));

        HistoricoCompra historico = mapearHistoricoCompraRequestDTOParaHistoricoCompra(request, produto);
        historicoCompraRepository.save(historico);

        return mapearParaHistoricoCompraResponseDTO(historico);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "historicoPorProduto", key = "#produtoId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<HistoricoCompraResponseDTO> listarHistoricoPorProduto(final Long produtoId, final Pageable pageable) {
        log.info("Listando histórico de compras do produto de ID {}", produtoId);
        
        if (!produtoRepository.existsById(produtoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado com o ID: " + produtoId);
        }
        
        return historicoCompraRepository.findByProdutoId(produtoId, pageable)
                .map(this::mapearParaHistoricoCompraResponseDTO);
    }

    private HistoricoCompra mapearHistoricoCompraRequestDTOParaHistoricoCompra(HistoricoCompraRequestDTO dto, Produto produto) {
        HistoricoCompra historico = new HistoricoCompra();
        historico.setProduto(produto);
        historico.setDataCompra(dto.dataCompra());
        historico.setQuantidade(dto.quantidade());
        historico.setPrecoUnitario(dto.precoUnitario());
        historico.setFornecedor(dto.fornecedor());
        return historico;
    }

    private HistoricoCompraResponseDTO mapearParaHistoricoCompraResponseDTO(HistoricoCompra historico) {
        return new HistoricoCompraResponseDTO(
                historico.getId(),
                historico.getProduto().getId(),
                historico.getProduto().getNome(),
                historico.getDataCompra(),
                historico.getQuantidade(),
                historico.getPrecoUnitario(),
                historico.getFornecedor()
        );
    }
}
