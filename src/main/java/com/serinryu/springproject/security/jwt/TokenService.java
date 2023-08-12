package com.serinryu.springproject.security.jwt;

import com.serinryu.springproject.security.PrincipalDetails;
import com.serinryu.springproject.entity.RefreshToken;
import com.serinryu.springproject.exception.InvalidMemberRoleException;
import com.serinryu.springproject.repository.RefreshTokenRepository;
import com.serinryu.springproject.service.RefreshTokenService;
import com.serinryu.springproject.service.UserDetailService;
import com.serinryu.springproject.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Duration;

/*
JwtProvider is a utility class for handling low-level JWT operations,
while the TokenService is a higher-level service that manages token-related operations specific to your application.
 */
@RequiredArgsConstructor
@Service
public class TokenService {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final String REDIRECT_PATH = "/blogs";

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailService userService;

    // Create a new access token based on the provided PrincipalDetails and set its expiration time.
    public String generateAccessToken(PrincipalDetails principalDetails) {
        return jwtProvider.generateToken(principalDetails, ACCESS_TOKEN_DURATION);
    }

    // Generate a new refresh token for the given user and save it in the database.
    public String generateAndSaveRefreshToken(PrincipalDetails principalDetails) {
        String refreshToken = jwtProvider.generateToken(principalDetails, REFRESH_TOKEN_DURATION); // Call the method to generate a new refresh token.
        saveRefreshToken(principalDetails.getId(), refreshToken);
        return refreshToken;
    }

    // 생성된 refreshToken 을 전달받아 DB에 저장
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));
        refreshTokenRepository.save(refreshToken);
    }


    // 생성된 refreshToken 을 쿠키에 저장
    public void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    // 쿠키에서 리다이렉트 경로가 담긴 값을 가져와 쿼리 파라미터에 accessToken 추가
    public String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }


    // 전달받은 refreshToken 으로 토큰 유효성 검사 진행하고,
    // 유효한 토큰이라면 1) 새로운 액세스 토큰 생성, 이 때 2) 리프레시 토큰도 다시 생성할 수 O
    public String createNewAccessToken(String refreshToken) {
        // 토큰 유효성 검사에 실패하면 예외 발생
        if(!jwtProvider.validToken(refreshToken)) {
            throw new InvalidMemberRoleException("Unexpected token");
        }

        Long userId = refreshTokenService.findByRefreshToken(refreshToken).getUserId();
        PrincipalDetails principalDetails = userService.findById(userId);

        //String newRefreshToken = generateAndSaveRefreshToken(principalDetails);

        return generateAccessToken(principalDetails);
    }

    public void deleteRefreshTokenCookie(HttpServletRequest request, HttpServletResponse response) {
        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
    }

}