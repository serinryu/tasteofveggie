package com.serinryu.springproject.config.jwt;

import com.serinryu.springproject.config.PrincipalDetails;
import com.serinryu.springproject.entity.RefreshToken;
import com.serinryu.springproject.repository.RefreshTokenRepository;
import com.serinryu.springproject.service.UserDetailService;
import com.serinryu.springproject.util.CookieUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.time.Duration;


@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final TokenService tokenService;
    private final UserDetailService userDetailService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String email = (String) userDetails.getUsername();
        log.info("🌈 앱 로그인 이메일 추출 :" + email);

        // 로그인 인증을 마친 사용자 가져오기
        PrincipalDetails user = userDetailService.findByEmail(email);

        // 1. refreshToken 생성 -> 저장 -> 쿠키에 저장
        String refreshToken = tokenService.generateAndSaveRefreshToken(user);
        tokenService.addRefreshTokenToCookie(request, response, refreshToken);
        log.info("🌈 refreshToken :" + refreshToken);

        // 2. accessToken 생성 -> 쿼리 파라미터에 accessToken 추가
        String accessToken = tokenService.generateAccessToken(user);
        String targetUrl = tokenService.getTargetUrl(accessToken);
        log.info("🌈 accessToken :" + accessToken);

        if (response.isCommitted()) {
            log.debug("response has already been committed. unable to redirect to " + targetUrl);
            return;
        }

        // 3. 인증 관련 설정값, 쿠키 제거 (세션에 저장된 객체를 다 사용한 뒤에는 지워줘서 메모리 누수 방지)
        clearAuthenticationAttributes(request, response);

        // 4. 리다이렉트
        getRedirectStrategy().sendRedirect(request, response, targetUrl);
        log.info(targetUrl);
    }


    // 인증 관련 설정값, 쿠키 제거
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
    }

}
