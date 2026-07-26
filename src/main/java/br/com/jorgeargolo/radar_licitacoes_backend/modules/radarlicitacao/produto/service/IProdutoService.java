package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.service;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.dto.request.ProdutoRequestDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.dto.response.ProdutoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * ‘Interface’ do serviço de Produto.
 */
public interface IProdutoService {

    /**
     * Cria um novo produto no catálogo.
     *
     * @param dto Dados do produto a ser salvo.
     * @return DTO com os dados do produto salvo.
     */
    ProdutoResponseDTO salvarProduto(final ProdutoRequestDTO dto);

    /**
     * Retorna a lista paginada de produtos.
     *
     * @param nome Filtro opcional pelo nome do produto.
     * @param pageable Configurações de paginação.
     * @return Página contendo os produtos encontrados.
     */
    Page<ProdutoResponseDTO> listarProdutos(final String nome, final Pageable pageable);

    /**
     * Busca um produto específico pelo ‘ID’.
     *
     * @param id Identificador do produto.
     * @return DTO com os dados do produto encontrado.
     * @throws RuntimeException se o produto não for encontrado.
     */
    ProdutoResponseDTO buscarPorId(final Long id);
}
