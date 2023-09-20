package com.invex.dto.profile;

import jakarta.validation.constraints.NotBlank;

public class CreateUserModuleDTO {
  @NotBlank(message = "Selecciona el tipo de acceso para el modulo usuarios")
  public String typeAccessId;
}
