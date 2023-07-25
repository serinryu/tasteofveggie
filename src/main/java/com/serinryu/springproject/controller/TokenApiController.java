package com.serinryu.springproject.controller;

import com.serinryu.springproject.dto.AccessTokenRequestDTO;
import com.serinryu.springproject.dto.JwtResponse;
import com.serinryu.springproject.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class TokenApiController {

    private final TokenService tokenService;

    @PostMapping("/api/token")
    public ResponseEntity<JwtResponse> createNewAccessToken(@RequestBody AccessTokenRequestDTO request) {
        // refreshToken 기반으로 새로운 액세스 토큰 제작 혹은 refreshToken 이 유효하지 않다면 TokenService 단에서 에러 던짐
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new JwtResponse(newAccessToken));
    }
}