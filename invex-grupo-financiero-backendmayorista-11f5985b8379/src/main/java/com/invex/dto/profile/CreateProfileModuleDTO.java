package com.invex.dto.profile;

import jakarta.validation.constraints.NotBlank;

public class CreateProfileModuleDTO {
  @NotBlank(message = "Selecciona el tipo de acceso para el modulo perfiles")
  public String typeAccessId;
}
