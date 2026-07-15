package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.repository;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long> {

}
