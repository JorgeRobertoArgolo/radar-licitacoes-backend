package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.dashboard.service;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.dashboard.dto.response.DashboardKpiResponseDTO;

/**
 * 'Interface' de serviço para o domínio de Dashboard (KPIs).
 */
public interface IDashboardService {

    /**
     * Retorna os indicadores consolidados (KPIs) do painel principal.
     *
     * @return DashboardKpiResponseDTO contendo as métricas agregadas.
     */
    DashboardKpiResponseDTO obterKpis();
}
