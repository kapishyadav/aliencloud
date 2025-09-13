package com.example.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class CustomOAuthUser implements OAuth2User{
    
    private final OAuth2User oAuth2User;

    public CustomOAuth2User(OAuth2User oAuth2User) {
        this.oAuth2User = oAuth2User;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        // Google: "name", GitHub: fallback to "login"
        Object name = oAuth2User.getAttributes().get("name");
        if (name == null) {
            name = oAuth2User.getAttributes().get("login"); // GitHub username
        }
        return name != null ? name.toString() : "Unknown";
    }

    public String getEmail() {
        Object email = oAuth2User.getAttributes().get("email");
        if (email == null) {
            // GitHub may hide email → fallback
            email = getName() + "@github.com";
        }
        return email.toString();
    }
    
}
