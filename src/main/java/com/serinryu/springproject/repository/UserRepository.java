package com.serinryu.springproject.repository;

import com.serinryu.springproject.entity.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserPrincipal, Long> {
    Optional<UserPrincipal> findByEmail(String email);
}
