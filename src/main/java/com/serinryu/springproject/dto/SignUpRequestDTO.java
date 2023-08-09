package com.serinryu.springproject.dto;

import com.serinryu.springproject.entity.Role;
import com.serinryu.springproject.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class SignUpRequestDTO {
    private String email;
    private String password;
    private Set<Long> roles;

    public User toEntity(Set<Role> roles) {
        return User.builder()
                .email(email)
                .password(password)
                .roles(roles)
                .build();
    }
}
