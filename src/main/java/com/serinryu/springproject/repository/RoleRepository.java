package com.serinryu.springproject.repository;

import com.serinryu.springproject.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRolename(String rolename);
    //Role findQneById(Long id);
}
