package com.serinryu.springproject.service;

import com.serinryu.springproject.config.jwt.JwtProvider;
import com.serinryu.springproject.entity.UserPrinciple;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;

    // 전달받은 refreshToken 으로 토큰 유효성 검사 진행하고, 유효한 토큰이라면 새로운 액세스 토큰 생성
    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!jwtProvider.validToken(refreshToken)) {
            throw new IllegalArgumentException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        UserPrinciple userPrinciple = userService.findById(userId);

        return jwtProvider.generateToken(userPrinciple, Duration.ofHours(2));
    }
}