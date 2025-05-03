package com.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class UserService implements UserDetailsService {
    // A dictionary (HashMap) of users and their passwords
    private final Map<String, String> users = new HashMap<>();
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Initialize users after bean creation
    @PostConstruct
    public void initUsers() {
        // Only initialize if no users are present
        if (users.isEmpty()) {
            users.put("admin", passwordEncoder.encode("password123"));
            users.put("guest1", passwordEncoder.encode("guest123"));
            users.put("user2", passwordEncoder.encode("user234"));
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Retrieve user from database
        if(!users.containsKey(username)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }

        //Retrieve password for username
        String password = users.get(username);

        return new User(username, password, new ArrayList<>());
    }
}