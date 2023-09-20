package com.invex.dto.profile;

import jakarta.validation.constraints.NotBlank;

public class CreateFlowModuleDTO {
  @NotBlank(message = "Selecciona el tipo de acceso para el modulo flujos")
  public String typeAccessId;
}
