package com.example.test.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Permission {
    @Id
    private Long id;
    private String name; // "CURRICULUM_UPDATE"
    private String description; // "Description of the permission"

    @ManyToOne
    private Feature feature;
}
