package com.invex.entities.profile;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "MODULO_ACCESO")
public class AccessModule extends PanacheEntityBase {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "moduloAccesoSeq")
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "NOMBRE", nullable = false)
  private String name;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "typeAccessId", referencedColumnName = "id", columnDefinition = "INT", nullable = false)
  private AccessType accessType;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "levelAccessId", referencedColumnName = "id", columnDefinition = "INT")
  private AccessLevel accessLevel;

  @JsonbTransient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "profileId")
  public Profile profile;
}
