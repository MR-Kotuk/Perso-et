package com.mrkotuk.PersoNet.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {
    private String token;
    private String message;
}
