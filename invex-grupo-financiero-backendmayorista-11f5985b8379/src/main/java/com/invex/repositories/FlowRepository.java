package com.invex.repositories;

import io.quarkus.hibernate.reactive.panache.PanacheRepositoryBase;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;

import com.invex.entities.flow.Flow;

@ApplicationScoped
public class FlowRepository implements PanacheRepositoryBase<Flow, Long> {
}
