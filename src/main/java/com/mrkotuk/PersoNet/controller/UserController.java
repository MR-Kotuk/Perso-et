package com.mrkotuk.PersoNet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mrkotuk.PersoNet.model.AuthResponse;
import com.mrkotuk.PersoNet.model.User;
import com.mrkotuk.PersoNet.service.UserService;

import lombok.AllArgsConstructor;

@CrossOrigin
@RestController
@AllArgsConstructor
public class UserController {
    private final UserService service;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        System.out.println("register");
        service.register(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {
        System.out.println("Login attempt for user: " + user.getUsername());

        String token = service.verify(user);

        if (token != null) {
            AuthResponse response = new AuthResponse();
            response.setToken(token);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            AuthResponse errorResponse = new AuthResponse();
            errorResponse.setMessage("Invalid credentials");
            return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);
        }
    }
}