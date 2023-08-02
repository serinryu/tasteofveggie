package com.serinryu.springproject.service;

import com.serinryu.springproject.dto.SignUpRequestDTO;
import com.serinryu.springproject.entity.User;
import com.serinryu.springproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(SignUpRequestDTO signUpRequestDTO) {

        User user = User.builder()
                .email(signUpRequestDTO.getEmail())
                .password(bCryptPasswordEncoder.encode(signUpRequestDTO.getPassword()))
                .build();

        userRepository.save(user);
        return user.getId();
    }
}
