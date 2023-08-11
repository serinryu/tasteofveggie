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

@RequiredArgsConstructor
@RestController
public class AuthApiController {

    private final UserService userService;
    private final TokenService tokenService;

    /**
     * Register a new user
     */
    @PostMapping("/api/signup")
    public ResponseEntity<String> signup(SignUpRequestDTO signUpRequestDTO){
        userService.save(signUpRequestDTO);
        return ResponseEntity.ok(signUpRequestDTO.toString());
    }

    /**
     * Logout the user
     */
    @GetMapping("/api/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // Delete the refresh token cookie
        tokenService.deleteRefreshTokenCookie(request, response);

        // Perform logout actions, clearing session, etc.
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return ResponseEntity.ok().build();
    }

}
