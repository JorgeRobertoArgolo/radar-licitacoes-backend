package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.model;

import br.com.jorgeargolo.radar_licitacoes_backend.infraestructure.model.SimplePersistenceEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "produtos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Produto extends SimplePersistenceEntity {

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "unidade_medida", nullable = false, length = 50)
    private String unidadeMedida;
}
