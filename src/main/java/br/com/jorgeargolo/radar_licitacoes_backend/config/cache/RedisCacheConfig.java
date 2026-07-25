package br.com.jorgeargolo.radar_licitacoes_backend.config.cache;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.cache.autoconfigure.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;

import java.time.Duration;

@Configuration
public class RedisCacheConfig implements CachingConfigurer {

    @Override
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }

    private static final Duration DEFAULT_TTL = Duration.ofMinutes(10);
    private static final Duration TTL_CATALOGO = Duration.ofHours(1);
    private static final Duration TTL_USUARIO = Duration.ofMinutes(2);

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration() {
        final ObjectMapper objectMapper = JsonMapper.builder()
                .activateDefaultTyping(
                        BasicPolymorphicTypeValidator.builder()
                                .allowIfBaseType(Object.class)
                                .build(),
                        ObjectMapper.DefaultTyping.NON_FINAL,
                        JsonTypeInfo.As.WRAPPER_ARRAY)
                .addModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
                .build();

        // Solução limpa: Implementação direta sem depender das classes em transição do Spring Data
        final RedisSerializer<Object> valueSerializer =
                new RedisSerializer<Object>() {
                    @Override
                    public byte[] serialize(Object t) {
                        try {
                            return t == null ? new byte[0] : objectMapper.writeValueAsBytes(t);
                        } catch (Exception e) {
                            throw new SerializationException("Erro ao serializar para JSON", e);
                        }
                    }

                    @Override
                    public Object deserialize(byte[] bytes) {
                        if (bytes == null || bytes.length == 0) return null;
                        try {
                            return objectMapper.readValue(bytes, Object.class);
                        } catch (Exception e) {
                            throw new SerializationException("Erro ao desserializar do JSON", e);
                        }
                    }
                };

        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(DEFAULT_TTL)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(valueSerializer));
    }

    /**
     * Sobrescreve o TTL padrão para regiões específicas do Radar de Licitações.
     */
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(
            final RedisCacheConfiguration baseConfig) {

        return builder -> builder
                .withCacheConfiguration("produtos", baseConfig.entryTtl(TTL_CATALOGO));
    }
}
