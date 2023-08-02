package com.serinryu.springproject.config;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccessTokenRequest {
    private String refreshToken;
}
