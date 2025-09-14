package com.example.model;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyAppUserRepository extends JpaRepository<MyAppUser, Long> {
    
    Optional<MyAppUser> findByUsername(String username);
    Optional<MyAppUser> findByProviderAndProviderId(String provider, String providerId);
    Optional<MyAppUser> findByEmail(String email);
}
