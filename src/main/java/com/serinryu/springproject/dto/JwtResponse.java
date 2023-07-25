package com.serinryu.springproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class JwtResponse {
    private String accessToken;
    //private String refreshToken;
}
