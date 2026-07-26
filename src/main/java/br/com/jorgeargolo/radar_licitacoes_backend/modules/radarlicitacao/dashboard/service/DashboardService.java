package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.dashboard.service;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.dashboard.dto.response.DashboardKpiResponseDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.repository.IHistoricoCompraRepository;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.repository.IProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Slf4j
@RequiredArgsConstructor
@Service
public class DashboardService implements IDashboardService {

    private final IProdutoRepository produtoRepository;
    private final IHistoricoCompraRepository historicoCompraRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "dashboardKpis", key = "'all'")
    public DashboardKpiResponseDTO obterKpis() {
        log.info("Calculando KPIs do dashboard");

        Long totalProdutos = produtoRepository.count();
        Long totalCompras = historicoCompraRepository.count();
        Long totalFornecedores = historicoCompraRepository.countDistinctFornecedores();
        BigDecimal valorTotal = historicoCompraRepository.calcularValorTotalCompras();

        return mapearParaDashboardKpiResponseDTO(totalProdutos, totalCompras, totalFornecedores, valorTotal);
    }

    private DashboardKpiResponseDTO mapearParaDashboardKpiResponseDTO(
            final Long totalProdutos,
            final Long totalCompras,
            final Long totalFornecedores,
            final BigDecimal valorTotal
    ) {
        return new DashboardKpiResponseDTO(
                totalProdutos,
                totalCompras,
                totalFornecedores,
                valorTotal
        );
    }
}
