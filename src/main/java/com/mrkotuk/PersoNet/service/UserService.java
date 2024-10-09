package com.mrkotuk.PersoNet.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.mrkotuk.PersoNet.model.User;
import com.mrkotuk.PersoNet.repo.UserRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepo repo;
    private final AuthenticationManager authManager;
    private final JWTService jwtService;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(7);

    public void register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        repo.save(user);
    }

    public String verify(User user) {
        Authentication authentication = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        System.out.println("Authenticated: " + authentication.isAuthenticated());

        return authentication.isAuthenticated()
                ? jwtService.generateToken(user.getUsername())
                : null;
    }

    public User findByUsername(String username) {
        return repo.findByUsername(username);
    }
}