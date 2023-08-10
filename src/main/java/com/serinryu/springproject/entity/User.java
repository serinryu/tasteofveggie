package com.serinryu.springproject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;

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

    @Column(name = "password", nullable=false)
    private String password;

    // OAuth2
    @Column(name = "nickname", unique = true)
    private String nickname;

    // role
    @ManyToMany(cascade=CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable(
            name="user_role",
            joinColumns={@JoinColumn(name="USER_ID", referencedColumnName="ID")},
            inverseJoinColumns={@JoinColumn(name="ROLE_ID", referencedColumnName="ID")})
    private Set<Role> roles;

    @Builder
    public User(String email, String password, String nickname, Set<Role> roles){
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.roles = roles;
    }

    public User update(String nickname){
        this.nickname = nickname;
        return this;
    }

}
