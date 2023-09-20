package com.invex.entities.profile;

import org.hibernate.annotations.ColumnDefault;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@Table(name = "TIPO_ACCESO")
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class AccessType extends PanacheEntityBase {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoAccesoSeq")
  @SequenceGenerator(name = "tipoAccesoSeq", sequenceName = "TIPO_ACCESO_SEQ", allocationSize = 1)
  @Column(name = "ID")
  private Long id;

  @Column(name = "NOMBRE", length = 200, nullable = false)
  private String name;

  @Column(name = "ESTATUS", nullable = false)
  @ColumnDefault("'1'")
  private Boolean status;
}
