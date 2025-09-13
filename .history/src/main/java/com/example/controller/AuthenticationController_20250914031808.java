package com.example.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.MyAppUser;
import com.example.security.CustomOAuthUser;

@RestController
public class AuthenticationController {

    @GetMapping("/authenticated")
    @ResponseBody
    public Map<String, Object> checkAuthentication(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            String username = null;
            String email = null;
            String provider = "local-db"; // default for database users

            if (principal instanceof MyAppUser user) {
                username = user.getUsername();
                email = user.getEmail();
                provider = user.getProvider() != null ? user.getProvider() : "local";
            } else if (principal instanceof CustomOAuthUser oauthUser) {
                username = oauthUser.getName();       // display name from OAuth provider
                email = oauthUser.getEmail();        // email from OAuth provider
                provider = "oauth";                  // generic provider label
            } else if (principal instanceof UserDetails userDetails) {
                username = userDetails.getUsername();
            }

            response.put("authenticated", true);
            response.put("username", username);
            response.put("email", email);
            response.put("provider", provider);
        } else {
            response.put("authenticated", false);
        }

        return response;
    }
}
