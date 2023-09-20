package com.invex.repositories;

import com.invex.entities.profile.AccessModule;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccessModuleRepository implements PanacheRepositoryBase<AccessModule, Long> {
}
