package com.invex.entities.profile;

import com.invex.entities.flow.Flow;
import com.invex.entities.user.User;
import jakarta.json.bind.annotation.JsonbTransient;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "PERFIL")
public class Profile extends PanacheEntityBase {

  @ManyToMany(fetch = FetchType.EAGER, mappedBy = "perfiles")
  private List<Flow> flujos = new ArrayList<>();

  @OneToMany(fetch = FetchType.EAGER, mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<User> users = new ArrayList<>();

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "perfilSeq")
  @SequenceGenerator(name = "perfilSeq", sequenceName = "PERFIL_SEQ", allocationSize = 1)
  @Column(name = "ID")
  private Long id;

  @Column(name = "NOMBRE")
  private String name;

  @Column(name = "CREA_SOLICITUDES")
  @ColumnDefault("'0'")
  private Boolean createRequests;

  @Column(name = "ACTIVO")
  @ColumnDefault("'1'")
  private Boolean isActive;

  @Column(name = "FECHA_DESACTIVA")
  private LocalDateTime dateDisabled;

  @Column(name = "FECHA_AUTORIZACION")
  private LocalDateTime dateAuthorization;

  @OneToMany(mappedBy = "profile", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  @Fetch(FetchMode.SUBSELECT)
  private List<AccessModule> modules = new ArrayList<>();
  
  @UpdateTimestamp
  @Column(name = "UPDATED_AT")
  private LocalDateTime updatedAt;

  @CreationTimestamp
  @Column(name = "CREATED_AT")
  private LocalDateTime createdAt;

  public List<Flow> getFlujos() {
    return flujos;
  }

  public List<User> getUser() {
    return users;
  }

  public void setUser(List<User> users) {
    this.users = users;
  }

  public void addUser(User user) {
    users.add(user);
    user.setProfile(this); // Mantener sincronización bidireccional
  }

  public void removeUser(User user) {
    users.remove(user);
    user.setProfile(null); // Mantener sincronización bidireccional
  }

  public void addFlow(Flow flow) {
    flujos.add(flow);
    flow.getPerfiles().add(this); // Mantener sincronización bidireccional
  }

  public void removeFlow(Flow flow) {
    flujos.remove(flow);
    flow.getPerfiles().remove(this); // Mantener sincronización bidireccional
  }

  public void setActive(Boolean active) {
  }
}
