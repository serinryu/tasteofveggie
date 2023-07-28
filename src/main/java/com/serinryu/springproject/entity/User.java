package com.serinryu.springproject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "password")
    private String password;

    // OAuth2
    @Column(name = "nickname", unique = true)
    private String nickname;

    @Builder
    public User(String email, String password, String nickname){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    public User update(String nickname){
        this.nickname = nickname;
        return this;
    }

}
