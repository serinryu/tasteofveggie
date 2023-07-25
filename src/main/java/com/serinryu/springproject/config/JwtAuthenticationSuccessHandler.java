package com.serinryu.springproject.config;

import com.serinryu.springproject.config.jwt.JwtProvider;
import com.serinryu.springproject.entity.UserPrinciple;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Duration;

@Slf4j
@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;

    public JwtAuthenticationSuccessHandler(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("로그인 성공");

        // 1. 로그인 인증을 마친 사용자 가져오기
        UserPrinciple userPrincipleDetails = (UserPrinciple) authentication.getPrincipal();

        // 2. 토큰 생성
        String token = jwtProvider.generateToken(userPrincipleDetails, Duration.ofDays(1));

        // 3. response
        response.addHeader("Authorization", "Bearer " + token);
        response.getWriter().write("로그인 완료되었습니다. 토큰 발급이 되었습니다.");
    }
}
