package com.invex.dto.user;

public class UserUpdateProfileDTO {

    private Long newProfileId;

    // Constructor sin parámetros (necesario para la deserialización JSON)
    public UserUpdateProfileDTO() {
    }

    // Constructor con parámetros (opcional)
    public UserUpdateProfileDTO(Long newProfileId) {
        this.newProfileId = newProfileId;
    }

    // Getters
    public Long getNewProfileId() {
        return newProfileId;
    }

    // Setters
    public void setNewProfileId(Long newProfileId) {
        this.newProfileId = newProfileId;
    }

    // Método toString (opcional, pero útil para debugging)
    @Override
    public String toString() {
        return "UserUpdateProfileDTO{" +
                "newProfileId=" + newProfileId +
                '}';
    }
}
