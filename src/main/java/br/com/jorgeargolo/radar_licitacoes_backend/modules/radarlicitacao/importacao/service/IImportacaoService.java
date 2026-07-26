package br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.importacao.service;

import br.com.jorgeargolo.radar_licitacoes_backend.modules.radarlicitacao.importacao.dto.response.ImportacaoResumoResponseDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 'Interface' de serviço para importação de dados via CSV.
 */
public interface IImportacaoService {

    /**
     * Processa um arquivo CSV contendo produtos e seus históricos de compras.
     *
     * @param file Arquivo CSV enviado pelo usuário.
     * @return DTO com o resumo da importação (quantidade de produtos e históricos processados).
     */
    ImportacaoResumoResponseDTO importarCsv(final MultipartFile file);
}
