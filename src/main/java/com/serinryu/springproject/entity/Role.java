package com.serinryu.springproject.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Set;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable=false, unique=true)
    private String rolename;

    @ManyToMany(mappedBy="roles", fetch = FetchType.EAGER) // mapped to the target User entity
    private Set<User> users;

    public Role(String rolename) {
        this.rolename = rolename;
    }
}
