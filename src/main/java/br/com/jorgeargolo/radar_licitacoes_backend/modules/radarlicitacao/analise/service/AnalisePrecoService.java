package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.analise.service;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.analise.dto.response.AnalisePrecoResponseDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.repository.IHistoricoCompraRepository;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.repository.projection.IEstatisticasProdutoProjection;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.repository.IProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.NormalDistribution;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Slf4j
@RequiredArgsConstructor
@Service
public class AnalisePrecoService implements IAnalisePrecoService {

    private final IHistoricoCompraRepository historicoCompraRepository;
    private final IProdutoRepository produtoRepository;

    @Override
    @Transactional(readOnly = true)
    public AnalisePrecoResponseDTO analisarProposta(final Long produtoId, final Double precoProposto) {
        log.info("Iniciando análise de preço para o produto de ID {}. Preço Proposto: {}", produtoId, precoProposto);

        if (!produtoRepository.existsById(produtoId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado com o ID: " + produtoId);
        }

        IEstatisticasProdutoProjection estatisticas = historicoCompraRepository.findEstatisticasByProdutoId(produtoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Estatísticas não puderam ser processadas"));

        Long qtdAmostras = estatisticas.getQuantidadeAmostras() != null ? estatisticas.getQuantidadeAmostras() : 0L;

        /// 1. Regra de Amostragem Mínima
        if (qtdAmostras < 3) {
            log.warn("Amostragem insuficiente ({}) para o produto {}", qtdAmostras, produtoId);
            return mapearParaAnalisePrecoResponseDTO(
                    false,
                    "Amostragem Insuficiente (menos de 3 registros). Requer análise manual do pregoeiro.",
                    estatisticas.getMedia(),
                    estatisticas.getDesvioPadrao(),
                    null,
                    null,
                    qtdAmostras
            );
        }

        Double media = estatisticas.getMedia();
        Double desvioPadrao = estatisticas.getDesvioPadrao();

        /// 2. O Problema da Divisão por Zero
        if (desvioPadrao == null || desvioPadrao == 0.0) {
            log.warn("Desvio padrão é zero para o produto {}", produtoId);
            if (precoProposto <= media) {
                return mapearParaAnalisePrecoResponseDTO(
                        false, 
                        "Preço aprovado. Preço proposto é menor ou igual à média e desvio padrão é zero.", 
                        media, desvioPadrao, null, null, qtdAmostras);
            } else {
                Double margemTolerancia = media * 1.05; /// Margem de tolerância de 5%
                if (precoProposto > margemTolerancia) {
                    return mapearParaAnalisePrecoResponseDTO(
                            true, 
                            "Risco de Superfaturamento! Preço 5% acima da média absoluta (Desvio Padrão = 0).", 
                            media, desvioPadrao, null, null, qtdAmostras);
                } else {
                    return mapearParaAnalisePrecoResponseDTO(
                            false, 
                            "Preço dentro da margem de tolerância aceitável.", 
                            media, desvioPadrao, null, null, qtdAmostras);
                }
            }
        }

        /// 3. Cálculo Matemático
        double escoreZ = (precoProposto - media) / desvioPadrao;
        
        /// Uso do Commons Math3 para pegar a probabilidade (qual porcentagem de preços da curva o valor supera)
        NormalDistribution normalDistribution = new NormalDistribution(media, desvioPadrao);
        double probabilidade = normalDistribution.cumulativeProbability(precoProposto);

        /// 4. Regra de Negócio (Anomalia Z > 2)
        if (escoreZ > 2.0) {
            log.warn("Alerta de sobrepreço gerado! Escore-Z: {}", escoreZ);
            return mapearParaAnalisePrecoResponseDTO(
                    true, 
                    String.format("Risco de Superfaturamento! Preço %.2f%% mais caro que os dados da amostra histórica normal.", probabilidade * 100), 
                    media, desvioPadrao, escoreZ, probabilidade, qtdAmostras);
        }

        return mapearParaAnalisePrecoResponseDTO(
                false, 
                "Preço aprovado. Dentro da curva estatística normal.", 
                media, desvioPadrao, escoreZ, probabilidade, qtdAmostras);
    }

    private AnalisePrecoResponseDTO mapearParaAnalisePrecoResponseDTO(
            Boolean risco, String msg, Double media, Double desvio,
            Double escoreZ, Double probabilidade, Long qtd
    ) {
        return new AnalisePrecoResponseDTO(risco, msg, media, desvio, escoreZ, probabilidade, qtd);
    }
}
