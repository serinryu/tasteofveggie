package com.serinryu.springproject.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    public CustomUsernamePasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            // 사용자가 전달한 JSON 데이터를 읽어와서 처리합니다.
            ObjectMapper objectMapper = new ObjectMapper();
            CustomLoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), CustomLoginRequest.class);

            // 커스텀 로직을 사용하여 사용자 인증을 처리합니다.
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(),
                    loginRequest.getPassword()
            );

            return authenticationManager.authenticate(authentication);
        } catch (IOException e) {
            throw new UsernameNotFoundException("Invalid request");
        }
    }

    // 필요한 경우 로그인 요청 데이터를 담을 클래스를 정의합니다.
    @Getter
    private static class CustomLoginRequest {
        private String email;
        private String password;

        // Getters and setters
    }


}
