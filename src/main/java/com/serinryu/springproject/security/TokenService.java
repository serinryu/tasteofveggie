package com.serinryu.springproject.security;

import com.serinryu.springproject.config.jwt.JwtProvider;
import com.serinryu.springproject.entity.RefreshToken;
import com.serinryu.springproject.entity.UserPrincipal;
import com.serinryu.springproject.exception.InvalidTokenException;
import com.serinryu.springproject.service.UserDetailService;
import com.serinryu.springproject.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class TokenService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailService userService;

    // Create a new access token based on the provided UserPrincipal and set its expiration time.
    public String generateAccessToken(UserPrincipal userPrincipal) {
        return jwtProvider.generateToken(userPrincipal, Duration.ofHours(2));
    }

    // Generate a new refresh token for the given user and save it in the database.
    public String generateAndSaveRefreshToken(UserPrincipal userPrincipal) {
        String refreshToken = generateRefreshToken(); // Call the method to generate a new refresh token.
        RefreshToken newRefreshToken = new RefreshToken(userPrincipal.getId(), refreshToken);
        refreshTokenService.save(newRefreshToken); // Save the new refresh token in the database.
        return refreshToken;
    }

    // Generate a new refresh token.
    private String generateRefreshToken() {
        // Implement the logic to generate a new refresh token.
        // For example, you can use a library like UUID to generate a unique refresh token.
        return UUID.randomUUID().toString();
    }

    // 전달받은 refreshToken 으로 토큰 유효성 검사 진행하고, 유효한 토큰이라면 새로운 액세스 토큰 생성
    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!jwtProvider.validToken(refreshToken)) {
            throw new InvalidTokenException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        UserPrincipal userPrincipal = userService.findById(userId);

        return jwtProvider.generateToken(userPrincipal, Duration.ofHours(2));
    }
}