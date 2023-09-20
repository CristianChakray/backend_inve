package com.invex.dto.profile;

import jakarta.validation.constraints.NotBlank;

public class CreateRequestModuleDTO {
  @NotBlank(message = "Selecciona el tipo de acceso para el modulo solicitudes")
  public String typeAccessId;
  public String levelAccessId;
}
