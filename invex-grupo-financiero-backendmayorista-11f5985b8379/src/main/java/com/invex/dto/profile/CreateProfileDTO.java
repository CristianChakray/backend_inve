package com.invex.dto.profile;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateProfileDTO {
  @NotBlank(message = "Ingresa el nombre del perfil")
  public String name;

  public Boolean createRequests;

  @Valid
  @NotNull(message = "Ingresa los datos para el modulo perfiles")
  public CreateProfileModuleDTO profileModule;

  @Valid
  @NotNull(message = "Ingresa los datos para el modulo usuarios")
  public CreateUserModuleDTO userModule;

  @Valid
  @NotNull(message = "Ingresa los datos para el modulo flujos")
  public CreateFlowModuleDTO flowModule;

  @Valid
  @NotNull(message = "Ingresa los datos para el modulo solicitudes")
  public CreateRequestModuleDTO requestModule;

  @Valid
  @NotNull(message = "Ingresa los datos para el modulo validaciones")
  public CreateValidationModuleDTO validationModule;
}
