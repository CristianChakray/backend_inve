package com.invex.dto.user;

import java.time.LocalDateTime;


public class UserDTO {

    private Long id;
    private String userName;
    private String userIdentifier;
    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;
    private UserStatus status;
    private ProfileSummaryDTO profile;

    public enum UserStatus {
        ACTIVO, INACTIVO
    }

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

    public ProfileSummaryDTO getProfile() {
        return profile;
    }

    public void setProfile(ProfileSummaryDTO profile) {
        this.profile = profile;
    }
}
