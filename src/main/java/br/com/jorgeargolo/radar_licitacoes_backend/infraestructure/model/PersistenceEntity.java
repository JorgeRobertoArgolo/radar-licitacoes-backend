package br.com.jorgeargolo.radar_licitacoes_backend.infraestructure.model;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.UUID;


/**
 * Classe base para entidades persistentes.
 *
 * <p>Fornece um identificador único gerado automaticamente para subclasses.
 * Pode ser usado como superclasse para outras entidades JPA.</p>
 *
 * @author Jorge Roberto
 */
@MappedSuperclass
public class PersistenceEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @EqualsAndHashCode.Include @ToString.Include
    @Getter private UUID id;
}
