package com.invex.dto.user;

public class UserSummaryDTO {

    private Long id;
    private String userName;
    private UserDTO.UserStatus status;

    //Getters y Setters

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

    public UserDTO.UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserDTO.UserStatus status) {
        this.status = status;
    }
}
