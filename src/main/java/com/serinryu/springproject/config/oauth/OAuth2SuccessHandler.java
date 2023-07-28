package com.serinryu.springproject.config.oauth;

import com.serinryu.springproject.config.jwt.JwtProvider;
import com.serinryu.springproject.config.PrincipalDetails;
import com.serinryu.springproject.repository.RefreshTokenRepository;
import com.serinryu.springproject.service.UserDetailService;
import com.serinryu.springproject.entity.RefreshToken;
import com.serinryu.springproject.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;

@RequiredArgsConstructor
@Component
@Slf4j
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    public static final String REFRESH_TOKEN_COOKIE_NAME = "refresh_token";
    public static final Duration REFRESH_TOKEN_DURATION = Duration.ofDays(14);
    public static final Duration ACCESS_TOKEN_DURATION = Duration.ofDays(1);
    public static final String REDIRECT_PATH = "/blogs";

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final OAuth2AuthorizationRequestBasedOnCookieRepository authorizationRequestRepository;
    private final UserDetailService userDetailService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        String email = (String) oAuth2User.getAttributes().get("email");

        logger.info("🌈 구글 인증 시 이메일 추출 :" + email);

        PrincipalDetails user = userDetailService.findByEmail(email);

        // 1. refreshToken 생성 -> 저장 -> 쿠키에 저장
        String refreshToken = jwtProvider.generateToken(user, REFRESH_TOKEN_DURATION);
        saveRefreshToken(user.getId(), refreshToken);
        addRefreshTokenToCookie(request, response, refreshToken);
        logger.info("🌈 refreshToken :" + refreshToken);

        // 2. accessToken 생성 -> 쿼리 파라미터에 accessToken 추가
        String accessToken = jwtProvider.generateToken(user, ACCESS_TOKEN_DURATION);
        String targetUrl = getTargetUrl(accessToken);
        logger.info("🌈 accessToken :" + accessToken);

        if (response.isCommitted()) {
            logger.debug("response has already been committed. unable to redirect to " + targetUrl);
            return;
        }

        // 3. 인증 관련 설정값, 쿠키 제거 (세션에 저장된 객체를 다 사용한 뒤에는 지워줘서 메모리 누수 방지)
        clearAuthenticationAttributes(request, response);

        // 4. 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    // 생성된 refreshToken 을 전달받아 DB에 저장
    private void saveRefreshToken(Long userId, String newRefreshToken) {
        RefreshToken refreshToken = refreshTokenRepository.findByUserId(userId)
                .map(entity -> entity.update(newRefreshToken))
                .orElse(new RefreshToken(userId, newRefreshToken));

        refreshTokenRepository.save(refreshToken);
    }

    // 생성된 refreshToken 을 쿠키에 저장
    private void addRefreshTokenToCookie(HttpServletRequest request, HttpServletResponse response, String refreshToken) {
        int cookieMaxAge = (int) REFRESH_TOKEN_DURATION.toSeconds();

        CookieUtil.deleteCookie(request, response, REFRESH_TOKEN_COOKIE_NAME);
        CookieUtil.addCookie(response, REFRESH_TOKEN_COOKIE_NAME, refreshToken, cookieMaxAge);
    }

    // 인증 관련 설정값, 쿠키 제거
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        authorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

    // 쿠키에서 리다이렉트 경로가 담긴 값을 가져와 쿼리 파라미터에 accessToken 추가
    private String getTargetUrl(String token) {
        return UriComponentsBuilder.fromUriString(REDIRECT_PATH)
                .queryParam("token", token)
                .build()
                .toUriString();
    }
}