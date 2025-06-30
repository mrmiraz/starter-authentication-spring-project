package com.example.test.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Permission {
    @Id
    private Long id;
    private String name; // e.g., "CURRICULUM_UPDATE"

    @ManyToOne
    private Feature feature;
}
