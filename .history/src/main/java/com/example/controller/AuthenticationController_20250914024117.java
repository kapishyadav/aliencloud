package com.example.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticationController {

    @GetMapping("/authenticated")
    @ResponseBody
    public Map<String, Object> checkAuthentication(Authentication authentication) {
        Map<String, Object> response = new HashMap<>();

        if (authentication != null && authentication.isAuthenticated()) {
            response.put("authenticated", true);
            response.put("username", authentication.getName());
            // Check if it's an OAuth2 login
            if (authentication instanceof OAuth2AuthenticationToken oauth2Token) {
                String provider = oauth2Token.getAuthorizedClientRegistrationId();
                response.put("provider", provider);

                Object principal = authentication.getPrincipal();

                if (principal instanceof OidcUser oidcUser) {
                    // Google (OIDC)
                    response.put("email", oidcUser.getEmail());
                    response.put("name", oidcUser.getFullName());
                    response.put("claims", oidcUser.getClaims()); // all claims
                } else if (principal instanceof OAuth2User oauth2User) {
                    // GitHub (OAuth2)
                    response.put("attributes", oauth2User.getAttributes());
                    response.put("name", oauth2User.getAttribute("name"));
                    response.put("email", oauth2User.getAttribute("email"));
                    response.put("login", oauth2User.getAttribute("login")); // GitHub username
                }
            }
            
        } else {
            response.put("authenticated", false);
        }

        return response;
    }
}
