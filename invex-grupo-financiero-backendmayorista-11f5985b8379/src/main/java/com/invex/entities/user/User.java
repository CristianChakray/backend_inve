package com.invex.entities.user;

import com.invex.entities.profile.Profile;
import io.quarkus.hibernate.reactive.panache.PanacheEntityBase;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "USERS")
public class User extends PanacheEntityBase {

    public enum UserStatus {
        ACTIVO, INACTIVO
    }
    @NotNull
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "PROFILE_ID", nullable = false)
    private Profile profile;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userSeq")
    @SequenceGenerator(name = "userSeq", sequenceName = "USER_SEQ", allocationSize = 1)
    @Column(name = "ID_USER")
    private Long id;

    @Column(name = "USER_NAME")
    private String userName;

    @Column(name = "USER_IDENTIFIER", unique = true)
    private String userIdentifier;

    @Column(name = "CREATION_DATE")
    private LocalDateTime creationDate;

    @Column(name = "LAST_MOD_DATE")
    private LocalDateTime lastModificationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private UserStatus status;

    public User() {
        this.status = UserStatus.ACTIVO;
        this.creationDate = LocalDateTime.now();
    }

    // Getters y Setters ...


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIdentifier() {
        return userIdentifier;
    }

    public void setUserIdentifier(String userIdentifier) {
        this.userIdentifier = userIdentifier;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(LocalDateTime lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    @PreUpdate
    public void onPreUpdate() {
        this.lastModificationDate = LocalDateTime.now();
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }
}
