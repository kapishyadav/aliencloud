package com.example.model;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.security.AppUserPrincipal;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyAppUserService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MyAppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        MyAppUser user = repository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return new AppUserPrincipal(user);
    }

    public MyAppUser loadUserEntity(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String provider = userRequest.getClientRegistration().getRegistrationId(); // google, github
        Map<String, Object> attributes = oAuth2User.getAttributes();

        final String email;
        final String displayName;
        final String providerId;

        switch (provider) {
            case "google" -> {
                email = (String) attributes.get("email");
                displayName = (String) attributes.get("name");
                providerId = (String) attributes.get("sub");
            }
            case "github" -> {
                String tmpEmail = (String) attributes.get("email");
                email = tmpEmail != null ? tmpEmail : attributes.get("login") + "@github.com";
                displayName = (String) attributes.get("name");
                providerId = String.valueOf(attributes.get("id"));
            }
            default -> throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + provider);
        }

        if (email == null) {
            throw new OAuth2AuthenticationException("No email found from " + provider + " OAuth2 provider");
        }

        // Ensure user is persisted in our DB
        return processOAuthPostLogin(provider, providerId, email, displayName);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId(); // google, github
        Map<String, Object> attributes = oAuth2User.getAttributes();

        final String email;
        final String displayName;
        final String providerId;

        switch (provider) {
            case "google" -> {
                email = (String) attributes.get("email");
                displayName = (String) attributes.get("name");
                providerId = (String) attributes.get("sub");
            }
            case "github" -> {
                String tmpEmail = (String) attributes.get("email");
                email = tmpEmail != null ? tmpEmail : attributes.get("login") + "@github.com";
                displayName = (String) attributes.get("name");
                providerId = String.valueOf(attributes.get("id"));
            }
            default -> throw new OAuth2AuthenticationException("Unsupported OAuth2 provider: " + provider);
        }

        if (email == null) {
            throw new OAuth2AuthenticationException("No email found from " + provider + " OAuth2 provider");
        }

        // Ensure user is persisted in our DB
        MyAppUser user = processOAuthPostLogin(provider, providerId, email, displayName);

        // Wrap in OAuth2User for Spring Security
        return new AppUserPrincipal(user, attributes);
    }

    /**
     * Ensures the OAuth user is persisted in our DB.
     * Order:
     *  1. Match by provider+providerId
     *  2. Fallback to email
     *  3. Insert new record
     */
    private MyAppUser processOAuthPostLogin(String provider, String providerId, String email, String displayName) {
        // 1. Check by provider + providerId
        return repository.findByProviderAndProviderId(provider, providerId)
            .orElseGet(() -> {
                // 2. Fallback: check by email
                return repository.findByEmail(email).map(user -> {
                    if (user.getProvider() == null) {
                        user.setProvider(provider);
                        user.setProviderId(providerId);
                        repository.save(user);
                    }
                    return user;
                }).orElseGet(() -> {
                    // 3. Create new user
                    MyAppUser newUser = new MyAppUser();
                    newUser.setUsername(email);
                    newUser.setEmail(email);
                    newUser.setDisplayName(displayName);
                    newUser.setProvider(provider);
                    newUser.setProviderId(providerId);
                    newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString())); // dummy password
                    newUser.setCreatedAt(Instant.now());
                    return repository.save(newUser);
                });
            });
    }
}
