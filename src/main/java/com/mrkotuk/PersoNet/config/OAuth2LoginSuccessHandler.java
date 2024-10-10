package com.mrkotuk.PersoNet.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mrkotuk.PersoNet.model.AuthResponse;
import com.mrkotuk.PersoNet.model.User;
import com.mrkotuk.PersoNet.repo.UserRepo;
import com.mrkotuk.PersoNet.service.JWTService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final UserRepo repo;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(7);

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {

        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
        OAuth2User oAuth2User = oauthToken.getPrincipal();

        User user = processOAuth2User(oAuth2User);

        String jwtToken = jwtService.generateToken(user.getUsername());

        if (jwtToken != null) {
            Cookie cookie = new Cookie("jwtToken", jwtToken);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(24 * 60 * 60);
            response.addCookie(cookie);

            AuthResponse authResponse = new AuthResponse();
            authResponse.setToken(jwtToken);
            authResponse.setUsername(user.getUsername());

            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_OK);

            ObjectMapper objectMapper = new ObjectMapper();

            response.getWriter().write(objectMapper.writeValueAsString(authResponse));
            System.out.println("JWT Token sent in response: " + jwtToken);
        } else {
            System.err.println("JWT Token generation failed for user: " + user.getUsername());
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "JWT Token generation failed");
            return;
        }

        System.out.println("User authenticated: " + user.getUsername());
        System.out.println("Authenticated: " + authentication.isAuthenticated());

        response.sendRedirect("http://127.0.0.1:5500/src/main/frontend/index.html");
    }

    public User processOAuth2User(OAuth2User oAuth2User) {
        String username = oAuth2User.getAttribute("name");
        String googleId = oAuth2User.getAttribute("sub");

        User user = repo.findByUsername(username);

        if (user == null) {
            user = new User();
            user.setUsername(username);
            user.setPassword(encoder.encode(googleId));
            repo.save(user);
        }

        return user;
    }
}