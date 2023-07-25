package com.serinryu.springproject.repository;

import com.serinryu.springproject.entity.UserPrinciple;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserPrinciple, Long> {
    Optional<UserPrinciple> findByEmail(String email);
}
