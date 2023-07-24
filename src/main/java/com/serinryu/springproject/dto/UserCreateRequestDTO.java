package com.serinryu.springproject.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequestDTO {
    private String email;
    private String password;
}
