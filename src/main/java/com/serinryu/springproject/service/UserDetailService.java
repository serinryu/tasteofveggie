package com.serinryu.springproject.service;

import com.serinryu.springproject.entity.UserPrincipal;
import com.serinryu.springproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService { // 실제 DB 로부터 사용자 정보를 가져옴
    private final UserRepository userRepository;

    // This method is used by Spring Security
    @Override
    public UserPrincipal loadUserByUsername(String email){
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException((email)));
    }

    public UserPrincipal findById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Unexpected user"));
    }

    public UserPrincipal findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Unexpected user"));
    }

}
