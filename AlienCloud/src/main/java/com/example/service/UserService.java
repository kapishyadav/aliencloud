package com.example.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {
    // A dictionary (HashMap) of users and their passwords
    private final Map<String, String> users = new HashMap<>();
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public UserService() {
        // Initialize with a default user
        users.put("admin", passwordEncoder.encode("{noop}password123"));
        users.put("guest1", passwordEncoder.encode("{noop}guest123"));
        users.put("user2", passwordEncoder.encode("{noop}user234"));
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