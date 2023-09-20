package com.invex.services;

import java.time.Duration;
import java.util.Collections;
import java.util.List;

import com.invex.entities.profile.AccessLevel;
import com.invex.entities.profile.AccessModule;
import com.invex.entities.profile.AccessType;
import com.invex.repositories.AccessLevelRepository;
import com.invex.repositories.AccessModuleRepository;
import com.invex.repositories.AccessTypeRepository;

import jakarta.inject.Inject;
import io.quarkus.hibernate.reactive.panache.Panache;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccessModuleService {
  @Inject
  AccessModuleRepository moduleRespository;

  @Inject
  AccessTypeRepository accessTypeRepository;

  @Inject
  AccessLevelRepository accessLevelRepository;

  public Uni<AccessModule> create(AccessModule module) {
    return moduleRespository.persistAndFlush(module)
        .onItem().ignore().andSwitchTo(Uni.createFrom().item(() -> module));
  }

  public AccessModule update(String name, List<AccessModule> modules, String _typeAccessId, String _levelAccessId) {
    Long typeAccessId = Long.parseLong(_typeAccessId);
    Long levelAccessId = null;
    if (_levelAccessId != null) levelAccessId = Long.parseLong(_levelAccessId);

    AccessModule moduleFind = modules.stream()
        .filter(module -> name.equals(module.getName()))
        .findAny()
        .orElse(null);
    AccessType accessType = new AccessType();
    accessType.setId(typeAccessId);
    moduleFind.setAccessType(accessType);

    if (levelAccessId != null) {
      AccessLevel accessLevel = new AccessLevel();
      accessLevel.setId(levelAccessId);
      moduleFind.setAccessLevel(accessLevel);
    } else {
      moduleFind.setAccessLevel(null);
    }

    return moduleFind;
  }

  public Uni<AccessModule> findByNameAndProfileId(String name, Long profileId) {
    return Panache
        .withTransaction(() -> moduleRespository.find("nombre = ?1 and profileId = ?2", name, profileId).firstResult());
  }

  public Uni<List<AccessModule>> getAll() {
    return moduleRespository.listAll()
        .ifNoItem()
        .after(Duration.ofMillis(10000))
        .fail()
        .onFailure()
        .recoverWithUni(Uni.createFrom().<List<AccessModule>>item(Collections.EMPTY_LIST));
  }

  public Uni<List<AccessType>> getAllAccessTypes() {
    return accessTypeRepository.listAll()
        .ifNoItem()
        .after(Duration.ofMillis(10000))
        .fail()
        .onFailure()
        .recoverWithUni(Uni.createFrom().<List<AccessType>>item(Collections.EMPTY_LIST));
  }

  public Uni<List<AccessLevel>> getAllAccessLevel() {
    return accessLevelRepository.listAll()
        .ifNoItem()
        .after(Duration.ofMillis(10000))
        .fail()
        .onFailure()
        .recoverWithUni(Uni.createFrom().<List<AccessLevel>>item(Collections.EMPTY_LIST));
  }
}
