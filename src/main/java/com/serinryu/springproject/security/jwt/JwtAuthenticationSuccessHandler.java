package com.serinryu.springproject.security.jwt;

import com.serinryu.springproject.security.PrincipalDetails;
import com.serinryu.springproject.service.UserDetailService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper; // Jackson 라이브러리 사용


import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

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
        log.info("🌈 accessToken :" + accessToken);

        //String targetUrl = tokenService.getTargetUrl(accessToken);

        if (response.isCommitted()) {
            log.debug("response has already been committed. unable to redirect");
            return;
        }

        // 3. 인증 관련 설정값, 쿠키 제거 (세션에 저장된 객체를 다 사용한 뒤에는 지워줘서 메모리 누수 방지)
        clearAuthenticationAttributes(request, response);

        // 4. 리다이렉트
        //getRedirectStrategy().sendRedirect(request, response, targetUrl);

        sendJsonResponse(response, accessToken);
    }

    private void sendJsonResponse(HttpServletResponse response, String accessToken) throws IOException {
        // JSON 응답 생성
        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("access_token", accessToken);
        responseBody.put("token_type", "Bearer");

        // JSON 응답 전송
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
    }


    // 인증 관련 설정값, 쿠키 제거
    private void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }

}
