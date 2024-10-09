package com.mrkotuk.PersoNet.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.model.User;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OAuth2UserService {
    private final UserService service;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;

    public ResponseEntity<String> verifyOauth2User(OAuth2User oAuth2User) {
        User user = processOAuth2User(oAuth2User);

        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        return authentication.isAuthenticated()
                ? new ResponseEntity<>(jwtService.generateToken(user.getUsername()), HttpStatus.OK)
                : new ResponseEntity<>("Fail", HttpStatus.NOT_ACCEPTABLE);
    }

    public ResponseEntity<String> registerByToken(OAuth2AuthenticationToken token) {
        User user = processOAuth2User(token.getPrincipal());

        return new ResponseEntity<>(jwtService.generateToken(user.getUsername()), HttpStatus.OK);
    }

    public User processOAuth2User(OAuth2User oAuth2User) {
        String username = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");

        User user = service.findByUsername(username);

        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword(googleId);
            service.register(user);
        }

        return user;
    }
}