package com.invex.services;

import jakarta.inject.Inject;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.logging.Logger;

import com.invex.dto.profile.CreateProfileDTO;
import com.invex.entities.profile.AccessLevel;
import com.invex.entities.profile.AccessModule;
import com.invex.entities.profile.AccessType;
import com.invex.entities.profile.Profile;
import com.invex.repositories.ProfileRepository;

import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProfileService {
  @Inject
  ProfileRepository profileRepository;

  @Inject
  AccessModuleService accessModuleService;

  private static final Logger LOG = Logger.getLogger(ProfileService.class);

  private Uni<Profile> updateProperties(Profile profile, Profile profileUpdated) {
    LocalDateTime date = LocalDateTime.now();
    profile.setCreateRequests(profileUpdated.getCreateRequests());
    profile.setName(profileUpdated.getName());
    profile.setIsActive(profileUpdated.getIsActive());
    profile.setUpdatedAt(date);
    return profile.persistAndFlush().replaceWith(profileUpdated);
  }

  public Uni<List<Profile>> getAll() {
    return profileRepository.listAll().ifNoItem()
        .after(Duration.ofMillis(10000))
        .fail()
        .onFailure()
        .recoverWithUni(Uni.createFrom().<List<Profile>>item(Collections.EMPTY_LIST));
  }

  public Uni<Profile> getById(Long id) {
    return Panache.withTransaction(() -> profileRepository.findById(id));
  }

  public Uni<Profile> create(CreateProfileDTO profileDTO) {
    Profile profile = new Profile();
    profile.setName(profileDTO.name);
    profile.setCreateRequests(profileDTO.createRequests);
    profile.setIsActive(true);

    AccessModule profileModule = new AccessModule();
    AccessType profileModuleAT = new AccessType();
    profileModule.setName("profiles");
    profileModuleAT.setId(Long.parseLong(profileDTO.profileModule.typeAccessId));
    profileModule.setAccessType(profileModuleAT);
    profileModule.setProfile(profile);

    AccessModule userModule = new AccessModule();
    AccessType userModuleAT = new AccessType();
    userModule.setName("users");
    userModuleAT.setId(Long.parseLong(profileDTO.userModule.typeAccessId));
    userModule.setAccessType(userModuleAT);
    userModule.setProfile(profile);

    AccessModule flowModule = new AccessModule();
    AccessType flowModuleAT = new AccessType();
    flowModule.setName("flows");
    flowModuleAT.setId(Long.parseLong(profileDTO.flowModule.typeAccessId));
    flowModule.setAccessType(flowModuleAT);
    flowModule.setProfile(profile);

    AccessModule requestModule = new AccessModule();
    AccessType requestModuleAT = new AccessType();
    AccessLevel requestModuleAL = new AccessLevel();
    requestModule.setName("requests");
    requestModuleAT.setId(Long.parseLong(profileDTO.requestModule.typeAccessId));
    requestModule.setAccessType(requestModuleAT);

    if (profileDTO.requestModule.levelAccessId != null) {
      requestModuleAL.setId(Long.parseLong(profileDTO.requestModule.levelAccessId));
      requestModule.setAccessLevel(requestModuleAL);
    }
    requestModule.setProfile(profile);

    AccessModule validationModule = new AccessModule();
    AccessType validationModuleAT = new AccessType();
    AccessLevel validationModuleAL = new AccessLevel();
    validationModule.setName("validations");
    validationModuleAT.setId(Long.parseLong(profileDTO.validationModule.typeAccessId));
    validationModule.setAccessType(validationModuleAT);

    if (profileDTO.validationModule.levelAccessId != null) {
      validationModuleAL.setId(Long.parseLong(profileDTO.validationModule.levelAccessId));
      validationModule.setAccessLevel(validationModuleAL);
    }
    validationModule.setProfile(profile);

    List<AccessModule> modules = new ArrayList<AccessModule>();
    modules.add(profileModule);
    modules.add(userModule);
    modules.add(flowModule);
    modules.add(requestModule);
    modules.add(validationModule);

    profile.setModules(modules);

    return profileRepository
        .persistAndFlush(profile)
        .onItem()
        .ignore()
        .andSwitchTo(Uni.createFrom().item(() -> profile));
  }

  public Uni<Profile> update(Long id, CreateProfileDTO profileDTO) {
    return Panache.withTransaction(() -> profileRepository.findById(id)
        .onItem()
        .ifNotNull()
        .transformToUni(profileExist -> {
          Profile updatePerfil = new Profile();
          updatePerfil.setName(profileDTO.name);
          updatePerfil.setCreateRequests(profileDTO.createRequests);
          updatePerfil.setIsActive(profileExist.getIsActive());
          updatePerfil.setCreatedAt(profileExist.getCreatedAt());

          List<AccessModule> modules = profileExist.getModules();
          AccessModule usersModule = accessModuleService.update("users", modules, profileDTO.userModule.typeAccessId,
              null);
          AccessModule profilesModule = accessModuleService.update("profiles", modules,
              profileDTO.profileModule.typeAccessId, null);
          AccessModule flowsModule = accessModuleService.update("flows", modules, profileDTO.flowModule.typeAccessId,
              null);
          AccessModule requestsModule = accessModuleService.update("requests", modules,
              profileDTO.requestModule.typeAccessId, profileDTO.requestModule.levelAccessId);
          AccessModule validationsModule = accessModuleService.update("validations", modules,
              profileDTO.validationModule.typeAccessId, profileDTO.validationModule.levelAccessId);

          List<AccessModule> modulesUpdated = new ArrayList<AccessModule>();
          modulesUpdated.add(usersModule);
          modulesUpdated.add(profilesModule);
          modulesUpdated.add(flowsModule);
          modulesUpdated.add(requestsModule);
          modulesUpdated.add(validationsModule);
          updatePerfil.setModules(modulesUpdated);

          return updateProperties(profileExist, updatePerfil);
        }));
  }

  public Uni<Profile> changeStatus(Long id, Boolean status) {
    return Panache.withTransaction(() -> profileRepository.findById(id)
        .onItem()
        .ifNotNull()
        .transformToUni(profileExist -> {
          Profile updatePerfil = new Profile();
          updatePerfil.setName(profileExist.getName());
          updatePerfil.setCreateRequests(profileExist.getCreateRequests());
          updatePerfil.setIsActive(status);
          updatePerfil.setModules(profileExist.getModules());
          updatePerfil.setCreatedAt(profileExist.getCreatedAt());
          if (!status) {
            LocalDateTime dateDisabled = LocalDateTime.now();
            updatePerfil.setDateDisabled(dateDisabled);
            updatePerfil.setUpdatedAt(dateDisabled);
          }

          return updateProperties(profileExist, updatePerfil);
        }));
  }
}
