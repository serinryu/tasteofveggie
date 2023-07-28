package com.serinryu.springproject.service;

import com.serinryu.springproject.config.PrincipalDetails;
import com.serinryu.springproject.entity.User;
import com.serinryu.springproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService { // 실제 DB 로부터 사용자 정보를 가져옴
    private final UserRepository userRepository;

    // "/login" 요청이 오면 자동으로 UserDetailsService 타입으로 loC 되어있는 loadUserByUsername 함수가 실행된다.
    // loadUserByUsername 메소드는 사용자의 패스워드 인증이 정상적으로 처리된 사용자의 이름을 반환해준다.
    // PrincipalDetails(UserDetails 상속)객체를 반환해주므로 Authentication 타입객체로 만들어질수 있고 스프링 시큐리티 세션안에 들어갈수 있게 된다.

    @Override
    public PrincipalDetails loadUserByUsername(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException((email)));
        return new PrincipalDetails(user);
    }

    public PrincipalDetails findById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("Unexpected user"));
        return new PrincipalDetails(user);
    }

    public PrincipalDetails findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Unexpected user"));
        return new PrincipalDetails(user);
    }

}
