package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.repository;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.model.HistoricoCompra;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.repository.projection.IEstatisticasProdutoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IHistoricoCompraRepository extends JpaRepository<HistoricoCompra, Long> {

    @Query(value = "SELECT AVG(preco_unitario) as media, STDDEV(preco_unitario) as desvioPadrao FROM historico_compras WHERE produto_id = :produtoId", nativeQuery = true)
    Optional<IEstatisticasProdutoProjection> findEstatisticasByProdutoId(@Param("produtoId") Long produtoId);
}
