package com.example.blog.service;

import com.example.blog.dto.UserCreateRequestDTO;
import com.example.blog.entity.User;
import com.example.blog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(UserCreateRequestDTO userCreateRequestDTO) {
        return userRepository.save(User.builder()
                .email(userCreateRequestDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(userCreateRequestDTO.getPassword()))
                .build()).getId();
    }
}
