package com.serinryu.springproject.config;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter @AllArgsConstructor
public class AccessTokenResponse {
    private String accessToken;
    //private String refreshToken;
}
