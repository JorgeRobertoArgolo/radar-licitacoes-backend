package br.com.jorgeargolo.radar_licitacoes_backend.infraestructure.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

/**
 * Classe base para entidades persistentes simples (usa 'long' como tipo de ‘ID’).
 *
 * <p>Fornece um identificador exclusivo gerado automaticamente para subclasses.
 * Pode ser usada como superclasse para outras entidades JPA.</p>
 *
 * @author Jorge Roberto
 */
@MappedSuperclass
public class SimplePersistenceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include @ToString.Include
    @Getter private Long id;
}
