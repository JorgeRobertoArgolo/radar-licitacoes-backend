package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.service;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.dto.request.HistoricoCompraRequestDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.dto.response.HistoricoCompraResponseDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * ‘Interface’ de serviço para o domínio de Histórico de Compra.
 */
public interface IHistoricoCompraService {

    /**
     * Salva um novo registro de histórico de compra no banco de dados.
     *
     * @param request DTO com os dados da compra
     * @return HistoricoCompraResponseDTO contendo os dados salvos
     */
    HistoricoCompraResponseDTO salvarHistorico(final HistoricoCompraRequestDTO request);

    /**
     * Lista os históricos de compra associados a um produto de forma paginada.
     *
     * @param produtoId ‘ID’ do produto
     * @param pageable Configurações de paginação
     * @return Página de HistoricoCompraResponseDTO
     */
    Page<HistoricoCompraResponseDTO> listarHistoricoPorProduto(final Long produtoId, final Pageable pageable);
}
