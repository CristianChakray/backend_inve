package com.invex.dto.user;

import java.time.LocalDateTime;
import java.util.List;

public class ProfileDTO extends ProfileSummaryDTO {

    private Boolean createRequests;
    private Boolean isActive;
    private LocalDateTime dateDisabled;
    private LocalDateTime dateAuthorization;
    private LocalDateTime updatedAt;
    private LocalDateTime createdAt;
    private List<UserSummaryDTO> users;

    public Boolean getCreateRequests() {
        return createRequests;
    }

    public void setCreateRequests(Boolean createRequests) {
        this.createRequests = createRequests;
    }

    public Boolean getActive() {
        return isActive;
    }

    public void setActive(Boolean active) {
        isActive = active;
    }

    public LocalDateTime getDateDisabled() {
        return dateDisabled;
    }

    public void setDateDisabled(LocalDateTime dateDisabled) {
        this.dateDisabled = dateDisabled;
    }

    public LocalDateTime getDateAuthorization() {
        return dateAuthorization;
    }

    public void setDateAuthorization(LocalDateTime dateAuthorization) {
        this.dateAuthorization = dateAuthorization;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<UserSummaryDTO> getUsers() {
        return users;
    }

    public void setUsers(List<UserSummaryDTO> users) {
        this.users = users;
    }
}
