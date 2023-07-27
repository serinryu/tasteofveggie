package com.serinryu.springproject.controller;

import com.serinryu.springproject.dto.SignUpRequestDTO;
import com.serinryu.springproject.security.TokenService;
import com.serinryu.springproject.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Slf4j
public class UserApiController {

    private final UserService userService;
    private final TokenService tokenService;

    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody SignUpRequestDTO signUpRequestDTO){
        userService.save(signUpRequestDTO);
        return ResponseEntity.ok(signUpRequestDTO.toString());
    }

    /* 현재는 스프링 시큐리티에서 제공하는 .formlogin() 을 사용하고 있음
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        UserPrincipal userPrinciple = userService.findByEmail(loginRequestDTO.getEmail());

        String accessToken = tokenService.generateAccessToken(userPrinciple);
        String refreshToken = tokenService.generateAndSaveRefreshToken(userPrinciple); // Implement this method to generate a new refresh token.

        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", accessToken);
        tokens.put("refresh_token", refreshToken);
        return ResponseEntity.ok(tokens);
    }
     */

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return "redirect:/login";
    }

}
