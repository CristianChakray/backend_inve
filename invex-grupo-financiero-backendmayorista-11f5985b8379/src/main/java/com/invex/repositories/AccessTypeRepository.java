package com.invex.repositories;

import com.invex.entities.profile.AccessType;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccessTypeRepository implements PanacheRepositoryBase<AccessType, Long>  {
  
}
