package com.invex.repositories;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import com.invex.entities.profile.Profile;

import java.util.List;

@ApplicationScoped
public class ProfileRepository implements PanacheRepositoryBase<Profile, Long> {
    public Uni<List<Profile>> findByIds(List<Long> perfilIds) {
        return list("id IN ?1", perfilIds);
    }

    public Uni<List<Profile>> listByIds(List<Long> profileIds) {
        return list("id IN ?1", profileIds);
    }
}
