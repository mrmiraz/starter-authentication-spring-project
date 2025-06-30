package com.example.test.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // The actual refresh token string
    @Column(nullable = false, unique = true)
    private String token;

    // When the token expires
    @Column(nullable = false)
    private Instant expiryDate;

    // Unique ID per browser or app instance (per session)
    @Column(nullable = true, unique = true)
    private String clientInstanceId;

    // Optional: Device-specific metadata
    private String deviceId;       // UUID for physical device
    private String deviceName;     // Phone 15, MacBook Pro
    private String platform;       // iOS, Android, Windows

    // Who this token belongs to
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;
}
