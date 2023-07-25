package com.serinryu.springproject.service;

import com.serinryu.springproject.dto.UserCreateRequestDTO;
import com.serinryu.springproject.entity.UserPrinciple;
import com.serinryu.springproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserPrinciple findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public UserPrinciple findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected user"));
    }

    public Long save(UserCreateRequestDTO userCreateRequestDTO) {
        return userRepository.save(UserPrinciple.builder()
                .email(userCreateRequestDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(userCreateRequestDTO.getPassword()))
                .build()).getId();
    }
}
