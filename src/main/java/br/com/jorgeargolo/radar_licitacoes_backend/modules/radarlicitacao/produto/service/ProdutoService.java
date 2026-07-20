package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.service;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.dto.request.ProdutoRequestDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.dto.response.ProdutoResponseDTO;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.model.Produto;
import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.produto.repository.IProdutoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;

@Slf4j
@RequiredArgsConstructor
@Service
public class ProdutoService implements IProdutoService {

    private final IProdutoRepository produtoRepository;

    @Override
    @Transactional
    @CacheEvict(value = "produtos", allEntries = true)
    public ProdutoResponseDTO salvarProduto(final ProdutoRequestDTO dto) {
        log.info("Salvando novo produto: {}", dto.nome());
        Produto produto = mapearProdutoRequestDTOParaProduto(dto);
        Produto produtoSalvo = produtoRepository.save(produto);
        return mapearParaProdutoResponseDTO(produtoSalvo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProdutoResponseDTO> listarProdutos(final Pageable pageable) {
        log.info("Listando produtos com paginação");
        return produtoRepository.findAll(pageable)
                .map(this::mapearParaProdutoResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "produtos", key = "#id")
    public ProdutoResponseDTO buscarPorId(final Long id) {
        log.info("Buscando produto pelo id: {}", id);
        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + id));
        return mapearParaProdutoResponseDTO(produto);
    }

    private ProdutoResponseDTO mapearParaProdutoResponseDTO(final Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getUnidadeMedida()
        );
    }

    private Produto mapearProdutoRequestDTOParaProduto(final ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setUnidadeMedida(dto.unidadeMedida());
        return produto;
    }
}
