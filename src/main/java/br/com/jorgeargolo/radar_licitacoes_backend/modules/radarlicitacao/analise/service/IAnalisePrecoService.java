package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.analise.service;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.analise.dto.response.AnalisePrecoResponseDTO;

/**
 * ‘Interface’ de serviço para o domínio de Análise de Preço.
 */
public interface IAnalisePrecoService {

    /**
     * Analisa uma proposta de preço usando Escore-Z e dados históricos,
     * detectando matematicamente o risco de superfaturamento.
     *
     * @param produtoId     ‘ID’ do produto analisado
     * @param precoProposto Preço proposto na licitação a ser analisado
     * @return AnalisePrecoResponseDTO contendo o resultado da análise e as métricas
     */
    AnalisePrecoResponseDTO analisarProposta(final Long produtoId, final Double precoProposto);
}
