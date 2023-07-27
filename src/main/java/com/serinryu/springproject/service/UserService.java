package com.serinryu.springproject.service;

import com.serinryu.springproject.dto.SignUpRequestDTO;
import com.serinryu.springproject.entity.UserPrincipal;
import com.serinryu.springproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(SignUpRequestDTO signUpRequestDTO) {
        return userRepository.save(UserPrincipal.builder()
                .email(signUpRequestDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpRequestDTO.getPassword()))
                .build()).getId();
    }
}
