package com.serinryu.springproject.service;

import com.serinryu.springproject.config.PrincipalDetails;
import com.serinryu.springproject.config.jwt.JwtProvider;
import com.serinryu.springproject.entity.RefreshToken;
import com.serinryu.springproject.exception.InvalidTokenException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
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

    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);

    // Create a new access token based on the provided PrincipalDetails and set its expiration time.
    public String generateAccessToken(PrincipalDetails principalDetails) {
        return jwtProvider.generateToken(principalDetails, ACCESS_TOKEN_DURATION);
    }

    // Generate a new refresh token for the given user and save it in the database.
    public String generateAndSaveRefreshToken(PrincipalDetails principalDetails) {
        String refreshToken = generateRefreshToken(); // Call the method to generate a new refresh token.
        RefreshToken newRefreshToken = new RefreshToken(principalDetails.getId(), refreshToken);
        refreshTokenService.save(newRefreshToken); // Save the new refresh token in the database.
        return refreshToken;
    }

    // Check if a refresh token exists for the given user in the database.
    public boolean hasRefreshToken(Long userId) {
        return refreshTokenService.findByUserId(userId).isPresent();
    }

    // Generate a new refresh token.
    private String generateRefreshToken() {
        // Implement the logic to generate a new refresh token.
        // For example, you can use a library like UUID to generate a unique refresh token.
        return UUID.randomUUID().toString();
    }

    // 전달받은 refreshToken 으로 토큰 유효성 검사 진행하고,
    // 유효한 토큰이라면 1) 새로운 액세스 토큰 생성, 이 때 2) 리프레시 토큰도 다시 생성할 수 O
    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!jwtProvider.validToken(refreshToken)) {
            throw new InvalidTokenException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        PrincipalDetails principalDetails = userService.findById(userId);

        //String newRefreshToken = generateAndSaveRefreshToken(principalDetails);

        return generateAccessToken(principalDetails);
    }

    public void deleteRefreshTokenCookie(HttpServletResponse response) {
        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
    }

}