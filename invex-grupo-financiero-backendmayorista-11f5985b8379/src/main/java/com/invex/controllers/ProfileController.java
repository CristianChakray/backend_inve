package com.invex.controllers;

import com.invex.dto.profile.CreateProfileDTO;
import com.invex.dto.profile.UpdateStatusDTO;
import com.invex.entities.profile.AccessLevel;
import com.invex.entities.profile.AccessType;
import com.invex.entities.profile.Profile;
import com.invex.services.AccessModuleService;
import com.invex.services.ProfileService;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.smallrye.mutiny.Uni;

import java.util.List;

import org.eclipse.microprofile.openapi.annotations.Operation;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("v1/profiles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProfileController {
  @Inject
  AccessModuleService moduleService;

  @Inject
  ProfileService profileService;

  @POST
  @Operation(summary = "Crea un nuevo perfil")
  @Path("")
  @WithSession
  public Uni<Profile> create(@Valid CreateProfileDTO profileDTO) {
    return profileService.create(profileDTO);
  }

  @GET
  @Operation(summary = "Lista de perfiles")
  @Path("")
  @WithSession
  public Uni<List<Profile>> getListProfile() {
    return profileService.getAll();
  }

  @GET
  @Operation(summary = "Busca perfil por ID")
  @Path("/{id}")
  public Uni<Profile> getById(@PathParam("id") Long id) {
    return profileService.getById(id);
  }


  @POST
  @Operation(summary = "Cambiar el estatus del perfil")
  @Path("/{id}/status")
  public Uni<Response> changeStatus(@PathParam("id") Long id, UpdateStatusDTO data) {
    return profileService.changeStatus(id, data.status)
        .onItem().ifNotNull().transform(profile -> Response.ok().status(Response.Status.OK).entity(profile).build())
        .onItem().ifNull().continueWith(Response.ok().status(Response.Status.NOT_FOUND)::build);
  }


  @PUT
  @Operation(summary = "Actualizar la información de un perfil")
  @Path("/{id}")
  public Uni<Response> update(@PathParam("id") Long id, CreateProfileDTO data) {
        return profileService.update(id, data)
        .onItem().ifNotNull().transform(profile -> Response.ok().status(Response.Status.OK).entity(profile).build())
        .onItem().ifNull().continueWith(Response.ok().status(Response.Status.NOT_FOUND)::build);
  }

  @GET
  @Operation(summary = "Lista de los tipos de acceso")
  @Path("access-types")
  @WithSession
  public Uni<List<AccessType>> getAccessTypes() {
    return moduleService.getAllAccessTypes();
  }

  @GET
  @Operation(summary = "Lista de los niveles de acceso")
  @Path("access-levels")
  @WithSession
  public Uni<List<AccessLevel>> getAccessLevels() {
    return moduleService.getAllAccessLevel();
  }
}
