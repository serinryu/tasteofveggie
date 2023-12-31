package com.serinryu.springproject.service;

import com.serinryu.springproject.entity.RefreshToken;
import com.serinryu.springproject.repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;

    // 전달받은 refreshToken 으로 refreshToken 을 검색해서 전달하는 메서드
    public RefreshToken findByRefreshToken(String refreshToken) {
        return refreshTokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("Unexpected token"));
    }

    public RefreshToken save(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<RefreshToken> findByUserId(Long userId) {
        return Optional.ofNullable(refreshTokenRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("Token exists for the given user")));
    }
}

