package com.example.test.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import java.util.Set;

@Entity
public class Feature {
    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "feature")
    private Set<Permission> permissions;
}
