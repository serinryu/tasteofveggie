package com.serinryu.springproject.security;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccessTokenRequest {
    private String refreshToken;
}
