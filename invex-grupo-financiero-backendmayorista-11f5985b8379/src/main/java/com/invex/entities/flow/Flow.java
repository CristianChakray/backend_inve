package com.invex.entities.flow;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.invex.entities.profile.Profile;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

@Entity
@Table(name = "FLUJOS")
public class Flow extends PanacheEntityBase {

  /**
   * Estados posibles para un flujo.
   */
  public enum EstadoFlujo {
    ACTIVO, INACTIVO
  }

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "flujoSeq")
  @SequenceGenerator(name = "flujoSeq", sequenceName = "FLUJO_SEQ", allocationSize = 1)
  @Column(name = "ID_FLUJO")
  private Long id;

  @Column(name = "NOMBRE_FLUJO", unique = true)
  private String nombreFlujo;

  @Column(name = "FECHA_CREACION")
  private LocalDateTime fechaCreacion;

  @Column(name = "FECHA_ULT_MOD")
  private LocalDateTime fechaUltimaModificacion;

  @Enumerated(EnumType.STRING)
  @Column(name = "ESTADO")
  private EstadoFlujo estado;

  @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
  @JoinTable(
          name = "PERFILES_FLUJOS",
          joinColumns = @JoinColumn(name = "ID_FLUJO", referencedColumnName = "ID_FLUJO"),
          inverseJoinColumns = @JoinColumn(name = "ID_PERFIL", referencedColumnName = "ID")
  )
  private List<Profile> perfiles = new ArrayList<>();

  /**
   * Constructor que inicializa valores por defecto.
   */
  public Flow() {
    this.estado = EstadoFlujo.ACTIVO;
    this.fechaCreacion = LocalDateTime.now();
  }

  // Getters y Setters (getters y setters para todos los atributos)

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getNombreFlujo() {
    return nombreFlujo;
  }

  public void setNombreFlujo(String nombreFlujo) {
    this.nombreFlujo = nombreFlujo;
  }

  public LocalDateTime getFechaCreacion() {
    return fechaCreacion;
  }

  public void setFechaCreacion(LocalDateTime fechaCreacion) {
    this.fechaCreacion = fechaCreacion;
  }

  public LocalDateTime getFechaUltimaModificacion() {
    return fechaUltimaModificacion;
  }

  public void setFechaUltimaModificacion(LocalDateTime fechaUltimaModificacion) {
    this.fechaUltimaModificacion = fechaUltimaModificacion;
  }

  public EstadoFlujo getEstado() {
    return estado;
  }

  public void setEstado(EstadoFlujo estado) {
    this.estado = estado;
  }

  /**
   * Callback ejecutado antes de la actualización para actualizar la fecha de
   * última modificación.
   */
  @PreUpdate
  public void onPreUpdate() {
    this.fechaUltimaModificacion = LocalDateTime.now();
  }

  public List<Profile> getPerfiles() {
    return perfiles;
  }

  public void setPerfiles(List<Profile> perfiles) {
    this.perfiles = perfiles;
  }

  public void addProfile(Profile profile) {
    List<Profile> modifiedPerfiles = new ArrayList<>(this.perfiles);
    modifiedPerfiles.add(profile);
    this.perfiles = modifiedPerfiles;
    profile.getFlujos().add(this);
  }


  public void removeProfile(Profile profile) {
    this.perfiles.remove(profile);
    profile.getFlujos().remove(this);
  }
}
