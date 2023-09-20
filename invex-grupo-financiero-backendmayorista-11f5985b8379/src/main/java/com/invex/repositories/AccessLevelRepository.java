package com.invex.repositories;

import com.invex.entities.profile.AccessLevel;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class AccessLevelRepository  implements PanacheRepositoryBase<AccessLevel, Long>  {
  
}
