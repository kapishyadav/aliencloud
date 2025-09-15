package com.example.model;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.springframework.security.crypto.password.PasswordEncoder;

public class MyAppUserServiceTest {
    
    private MyAppUserRepository repository;
    private PasswordEncoder passwordEncoder;
    private MyAppUserService userService;

    @BeforeEach
    void setup() {
        repository = mock(MyAppUserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        userService = new MyAppUserService(repository, passwordEncoder);
    }

    @Test
    void loadUserByUsername_existingUser_returnsUserDetails() {
        MyAppUser user = new MyAppUser();
        user.setUsername("test@example.com");
        when(repository.findByUsername("test@example.com")).thenReturn(Optional.of(user));

        var userDetails = userService.loadUserByUsername("test@example.com");

        assertEquals("test@example.com", userDetails.getUsername());
    }

    @Test
    void processOAuthPostLogin_createsNewUser_ifNotExist() {
        String provider = "google";
        String providerId = "123";
        String email = "google@example.com";
        String displayName = "Google User";

        // 1. No user exists by provider+providerId
        when(repository.findByProviderAndProviderId(provider, providerId))
            .thenReturn(Optional.empty());

        // 2. No user exists by email
        when(repository.findByEmail(email))
            .thenReturn(Optional.empty());

        // 3. Mock password encoding
        when(passwordEncoder.encode(anyString()))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // 4. Mock save to return the saved user
        when(repository.save(any(MyAppUser.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Call method
        MyAppUser createdUser = userService.processOAuthPostLogin(provider, providerId, email, displayName);

        // Assertions
        assertNotNull(createdUser);
        assertEquals(email, createdUser.getEmail());
        assertEquals(provider, createdUser.getProvider());
        assertEquals(providerId, createdUser.getProviderId());
    }
}
