package com.invex.dto.profile;

import jakarta.validation.constraints.NotBlank;

public class CreateValidationModuleDTO {
  @NotBlank(message = "Selecciona el tipo de acceso para el modulo validaciones")
  public String typeAccessId;
  public String levelAccessId;
}
