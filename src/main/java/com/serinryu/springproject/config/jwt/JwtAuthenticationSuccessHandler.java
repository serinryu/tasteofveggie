package com.serinryu.springproject.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serinryu.springproject.dto.ApiResponse;
import com.serinryu.springproject.config.PrincipalDetails;
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
        PrincipalDetails principalDetailsDetails = (PrincipalDetails) authentication.getPrincipal();

        // 2. 토큰 생성
        String token = jwtProvider.generateToken(principalDetailsDetails, Duration.ofDays(1));

        // 3. response
        response.addHeader("Authorization", "Bearer " + token);

        ApiResponse apiResponse = new ApiResponse(true, "Successfully Logged in. Token is issued");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(jsonResponse);
    }
}
