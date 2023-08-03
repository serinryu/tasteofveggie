package com.serinryu.springproject.security;

import com.serinryu.springproject.security.jwt.TokenService;
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

    @PostMapping("/api/issue-new-token")
    public ResponseEntity<AccessTokenResponse> createNewAccessToken(@RequestBody AccessTokenRequest request) {
        // refreshToken 기반으로 새로운 accessToken 제작
        String newAccessToken = tokenService.createNewAccessToken(request.getRefreshToken());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new AccessTokenResponse(newAccessToken));
    }
}