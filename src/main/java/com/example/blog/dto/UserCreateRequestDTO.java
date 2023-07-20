package com.example.blog.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreateRequestDTO {
    private String email;
    private String password;
}
