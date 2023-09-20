package com.invex.repositories;

import com.invex.entities.user.User;
import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;


import java.util.List;

@ApplicationScoped
public class UserRepository implements PanacheRepositoryBase<User, Long> {
    public Uni<List<User>> findPaginated(int page, int size) {
        return User.findAll().page(Page.of(page, size)).list();
    }
}
