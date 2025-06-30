package com.example.test.domain.entity;
import com.example.test.enums.UserType;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String type;
    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;
}
