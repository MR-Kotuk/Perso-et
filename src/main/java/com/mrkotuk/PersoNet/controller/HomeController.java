package com.mrkotuk.PersoNet.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class HomeController {
    @GetMapping("/")
    public ResponseEntity<String> greed() {
        return new ResponseEntity<>("Hello, you logined to Perso|||et", HttpStatus.OK);
    }
}
