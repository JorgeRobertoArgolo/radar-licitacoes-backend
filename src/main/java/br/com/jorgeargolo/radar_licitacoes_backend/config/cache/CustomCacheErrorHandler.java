package br.com.jorgeargolo.radar_licitacoes_backend.config.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.interceptor.CacheErrorHandler;

/**
 * Handler customizado para tratar erros de cache (ex: Redis ‘offline’).
 * Quando o provedor de cache estiver indisponível, a aplicação continua
 * a funcionar normalmente, apenas sem o benefício do cache.
 */
@Slf4j
public class CustomCacheErrorHandler implements CacheErrorHandler {

    @Override
    public void handleCacheGetError(RuntimeException exception, Cache cache, Object key) {
        log.warn("Falha ao buscar do cache '{}' com a chave '{}': {}", cache.getName(), key, exception.getMessage());
    }

    @Override
    public void handleCachePutError(RuntimeException exception, Cache cache, Object key, Object value) {
        log.warn("Falha ao gravar no cache '{}' com a chave '{}': {}", cache.getName(), key, exception.getMessage());
    }

    @Override
    public void handleCacheEvictError(RuntimeException exception, Cache cache, Object key) {
        log.warn("Falha ao invalidar o cache '{}' com a chave '{}': {}", cache.getName(), key, exception.getMessage());
    }

    @Override
    public void handleCacheClearError(RuntimeException exception, Cache cache) {
        log.warn("Falha ao limpar o cache '{}': {}", cache.getName(), exception.getMessage());
    }
}
