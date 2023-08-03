package com.serinryu.springproject.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serinryu.springproject.dto.ApiResponse;
import com.serinryu.springproject.config.PrincipalDetails;
import com.serinryu.springproject.service.TokenService;
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
    private final TokenService tokenService;

    public JwtAuthenticationSuccessHandler(TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        log.info("로그인 성공");

        // 1. 로그인 인증을 마친 사용자 가져오기
        PrincipalDetails principalDetailsDetails = (PrincipalDetails) authentication.getPrincipal();

        // 2. 액세스 토큰 생성
        String accessToken = tokenService.generateAccessToken(principalDetailsDetails);

        // 3. response
        response.addHeader("Authorization", "Bearer " + accessToken);

        ApiResponse apiResponse = new ApiResponse(true, "Successfully Logged in. Token is issued");
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonResponse = objectMapper.writeValueAsString(apiResponse);
        response.getWriter().write(jsonResponse);
    }
}
