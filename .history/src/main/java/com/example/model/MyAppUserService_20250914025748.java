package com.example.model;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MyAppUserService implements UserDetailsService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MyAppUserRepository repository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = new DefaultOAuth2UserService().loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId(); // google, github
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = null;
        String displayName = null;

        if ("google".equals(provider)) {
            email = (String) attributes.get("email");
            displayName = (String) attributes.get("name");
        } else if ("github".equals(provider)) {
            email = (String) attributes.get("email");
            displayName = (String) attributes.get("name");
            if (email == null) {
                email = attributes.get("login") + "@github.com"; // fallback
            }
        }

        if (email == null) {
            throw new OAuth2AuthenticationException("No email found from " + provider + " OAuth2 provider");
        }

        // Check if user already exists
        MyAppUser user = repository.findByUsername(email).orElseGet(() -> {
            MyAppUser newUser = new MyAppUser();
            newUser.setUsername(name);
            newUser.setEmail(email);
            newUser.setProvider(provider);
            newUser.setPassword(passwordEncoder.encode("oauth2user")); // dummy
            return repository.save(newUser);
        });

        // Wrap in OAuth2User for Spring Security
        return new DefaultOAuth2User(
                Set.of(() -> "ROLE_USER"),
                attributes,
                "email" // the key in attributes map to use as principal name
        );
    }
}
