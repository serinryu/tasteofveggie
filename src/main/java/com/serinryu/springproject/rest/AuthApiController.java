package com.serinryu.springproject.rest;

import com.serinryu.springproject.dto.SignUpRequestDTO;
import com.serinryu.springproject.security.jwt.TokenService;
import com.serinryu.springproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final UserService userService;
    private final TokenService tokenService;

    /**
     * Register a new user
     */
    @PostMapping("/api/signup")
    public ResponseEntity<Map<String, Object>> signup(SignUpRequestDTO signUpRequestDTO){

        userService.save(signUpRequestDTO);
        // long userId = ....

        Map<String, Object> response = new HashMap<>();
        response.put("message", "회원 가입이 성공적으로 완료되었습니다.");
        response.put("timestamp", LocalDateTime.now());

        Map<String, Object> data = new HashMap<>();
        //data.put("userId", userId);
        response.put("data", data);

        return ResponseEntity.ok().body(response);
    }

    /**
     * Logout the user
     */
    @GetMapping("/api/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request, HttpServletResponse response) {
        // Delete the refresh token cookie
        tokenService.deleteRefreshTokenCookie(request, response);

        // Perform logout actions, clearing session, etc.
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("message", "로그아웃이 성공적으로 완료되었습니다.");
        responseBody.put("timestamp", LocalDateTime.now());

        return ResponseEntity.ok().body(responseBody);
    }

}
