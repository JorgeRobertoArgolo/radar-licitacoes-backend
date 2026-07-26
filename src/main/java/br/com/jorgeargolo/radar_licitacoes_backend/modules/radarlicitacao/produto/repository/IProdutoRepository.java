package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.repository;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IProdutoRepository extends JpaRepository<Produto, Long> {

    Page<Produto> findByNomeContainingIgnoreCase(String nome, Pageable pageable);

    java.util.Optional<Produto> findByNomeIgnoreCase(String nome);
}
