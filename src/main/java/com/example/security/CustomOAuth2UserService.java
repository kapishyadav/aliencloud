package com.example.security;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.example.model.MyAppUser;
import com.example.model.MyAppUserService;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final MyAppUserService myAppUserService;

    public CustomOAuth2UserService(MyAppUserService myAppUserService) {
        this.myAppUserService = myAppUserService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest)
            throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId(); // "google" or "github"
        String providerId = oAuth2User.getAttribute("sub"); // Google unique ID
        String email = oAuth2User.getAttribute("email");
        String displayName = oAuth2User.getAttribute("name");

        MyAppUser user = myAppUserService.processOAuthPostLogin(provider, providerId, email, displayName);

        return new AppUserPrincipal(user, oAuth2User.getAttributes());
    }
}
