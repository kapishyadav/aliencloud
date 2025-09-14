package com.example.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class MyAppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String username;
    private String email;
    private String password;
    private String name;
    private String displayName;
    private Instant createdAt = Instant.now();

    private String provider;   // "local", "google", "github"
    private String providerId; // provider-specific user ID

    private Boolean locked = false;
    private Boolean enabled = true;
    
}
