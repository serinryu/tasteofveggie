package com.serinryu.springproject.rest;

import com.serinryu.springproject.dto.SignUpRequestDTO;
import com.serinryu.springproject.security.jwt.TokenService;
import com.serinryu.springproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class UserApiController {

    private final UserService userService;
    private final TokenService tokenService;
    private AuthenticationManager authenticationManager;


    @GetMapping("/api/admin")
    public ResponseEntity<String> getAdminPage(Authentication authentication){
        System.out.println(authentication.getAuthorities());
        return ResponseEntity.ok("ADMIN PAGE");
    }

    /*
    @GetMapping("/api/user")
    public ResponseEntity<UserResponseDTO> getUserInfo(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            // 사용자가 인증되지 않았을 경우 처리
            return new UserResponseDTO("Anonymous", false);
        }

        User user = (User) authentication.getPrincipal();
        String username = user.getUsername();
        boolean isAuthenticated = authentication.isAuthenticated();

        return new UserResponseDTO(username, isAuthenticated);
    }

     */


    @PostMapping("/signup")
    public ResponseEntity<String> signup(SignUpRequestDTO signUpRequestDTO){
        userService.save(signUpRequestDTO);
        return ResponseEntity.ok(signUpRequestDTO.toString());
    }

    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // Delete the refresh token cookie
        tokenService.deleteRefreshTokenCookie(request, response);

        // Perform logout actions, clearing session, etc.
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());

        return ResponseEntity.ok().build();
    }

}