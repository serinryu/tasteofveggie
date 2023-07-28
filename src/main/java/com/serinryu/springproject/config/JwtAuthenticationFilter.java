package com.serinryu.springproject.config;


import com.serinryu.springproject.config.jwt.JwtProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/*
액세스 토큰값이 담긴 Authorization 헤더값을 가져온 뒤 액세스 토큰이 유효하다면 인증 정보 설정
 */
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer "; // 인증 토큰의 인증 타입(grant type)

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)  throws ServletException, IOException {

        // 헤더에서 JWT 토큰을 받아옵니다.
        String authorizationHeader = request.getHeader(HEADER_AUTHORIZATION);
        String token = getAccessToken(authorizationHeader);

        // 토큰이 들어오는지, 들어온다면 유효한 토큰인지 확인합니다.
        if (token != null && jwtProvider.validToken(token)) {
            // 헤더에서 추출한 토큰을 기반으로 JwtAuthenticationToken 객체를 만들고, AuthenticationManager 객체에게 전달하여 인증을 요청합니다.
            Authentication authentication = jwtProvider.getAuthentication(token);
            // SecurityContextHolder 클래스에 담습니다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);

    }

    private String getAccessToken(String authorizationHeader) {
        // 헤더에 Authorization 키가 존재하는지 확인합니다.
        // 인증 토큰의 인증 타입(grant type)이 Bearer 인지 확인합니다.
        if (authorizationHeader != null && authorizationHeader.startsWith(TOKEN_PREFIX)) {
            return authorizationHeader.substring(TOKEN_PREFIX.length());
        }

        return null;
    }

}