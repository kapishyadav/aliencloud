package com.example.security;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import com.example.model.MyAppUser;

public class AppUserPrincipal implements UserDetails, OAuth2User {

    private final MyAppUser user;      // persisted DB user
    private final Map<String, Object> attributes; // raw OAuth2 attributes

    public AppUserPrincipal(MyAppUser user) {
        this.user = user;
        this.attributes = Collections.emptyMap();
    }

    public AppUserPrincipal(MyAppUser user, Map<String, Object> attributes) {
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("USER"));
    }

    @Override
    public String getName() {
        // Use "displayname"; else fallback to "username"
        Object name = user.getDisplayName();
        if (name == null) {
            name = user.getUsername(); // get username
        }
        return name != null ? name.toString() : "Unknown";
    }

    public String getEmail() {
        Object email = attributes.get("email");
        if (email == null) {
            // GitHub may hide email → fallback
            email = getName() + "@github.com";
        }
        return email.toString();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !user.getLocked();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return user.getEnabled();
    }

    // === OAuth2User methods ===
    public String getProvider() {
        return user.getProvider();
    }

    public String getProviderId() {
        return user.getProviderId();
    }

    public MyAppUser getUser() {
        return user;
    }

}
