package com.invex.entities.profile;

import org.hibernate.annotations.ColumnDefault;

import jakarta.persistence.*;

@Entity
@Table(name = "NIVEL_ACCESO")
public class AccessLevel {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "nivel_acceso_seq")
  @SequenceGenerator(name = "nivel_acceso_seq", sequenceName = "NIVEL_ACCESO_SEQ", allocationSize = 1)
  @Column(name = "ID", nullable = false)
  private Long id;

  @Column(name = "NOMBRE", length = 200, nullable = false)
  private String name;

  @Column(name = "ESTATUS")
  @ColumnDefault("'1'")
  private Boolean status;

  public AccessLevel() {
  }

  public AccessLevel(Long id, String name) {
    this.id = id;
    this.name = name;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }
}
