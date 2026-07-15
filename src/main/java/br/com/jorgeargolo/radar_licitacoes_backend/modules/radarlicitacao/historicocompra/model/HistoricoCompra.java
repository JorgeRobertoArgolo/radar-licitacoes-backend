package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.historicocompra.model;

import br.com.jorgeargolo.radar_licitacoes_backend.infraestructure.model.SimplePersistenceEntity;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.model.Produto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "historico_compras")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class HistoricoCompra extends SimplePersistenceEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @Column(name = "data_compra", nullable = false)
    private LocalDate dataCompra;

    @Column(name = "quantidade", nullable = false)
    private Integer quantidade;

    @Column(name = "preco_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal precoUnitario;

    @Column(name = "fornecedor")
    private String fornecedor;
}
