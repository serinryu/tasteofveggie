package com.serinryu.springproject.service;

import com.serinryu.springproject.entity.User;
import com.serinryu.springproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService { // 실제 DB 로부터 사용자 정보를 가져옴
    private final UserRepository userRepository;

    @Override
    public User loadUserByUsername(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException((email)));
    }
}