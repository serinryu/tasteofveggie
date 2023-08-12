package com.serinryu.springproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AccessTokenRequestDTO {
    private String refreshToken;
}
