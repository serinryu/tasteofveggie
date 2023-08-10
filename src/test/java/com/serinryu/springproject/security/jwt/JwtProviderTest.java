package com.serinryu.springproject.security.jwt;

import com.serinryu.springproject.entity.Role;
import com.serinryu.springproject.repository.RoleRepository;
import com.serinryu.springproject.security.PrincipalDetails;
import com.serinryu.springproject.entity.User;
import com.serinryu.springproject.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class JwtProviderTest {

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProperties jwtProperties;

    @Autowired
    private RoleRepository roleRepository;

    @DisplayName("generateToken(): 유저 정보와 만료 기간을 전달해 토큰을 만들 수 있다.")
    @Test
    @Transactional
    void generateToken() {
        // given
        Set<Role> rolesSet = new HashSet<>();
        rolesSet.add(roleRepository.findByRolename("ROLE_USER").get());

        User user = userRepository.save(
                User.builder()
                .email("user1@gmail.com")
                .password("test")
                .roles(rolesSet)
                .build()
        );

        PrincipalDetails testPrincipalDetails = new PrincipalDetails(user);

        // when
        String token = jwtProvider.generateToken(testPrincipalDetails, Duration.ofDays(14));

        // then
        Long userId = Jwts.parser()
                .setSigningKey(jwtProperties.getSecretKey())
                .parseClaimsJws(token)
                .getBody()
                .get("id", Long.class);

        assertThat(userId).isEqualTo(testPrincipalDetails.getId());
    }

    @DisplayName("validToken(): 만료된 토큰인 경우에 유효성 검증에 실패한다.")
    @Test
    @Transactional
    void validToken_invalidToken() {
        // given
        String token = JwtFactory.builder()
                .expiration(new Date(new Date().getTime() - Duration.ofDays(7).toMillis()))
                .build()
                .createToken(jwtProperties);

        // when
        boolean result = jwtProvider.validToken(token);

        // then
        assertThat(result).isFalse();
    }


    @DisplayName("validToken(): 유효한 토큰인 경우에 유효성 검증에 성공한다.")
    @Test
    @Transactional
    void validToken_validToken() {
        // given
        String token = JwtFactory.withDefaultValues()
                .createToken(jwtProperties);

        // when
        boolean result = jwtProvider.validToken(token);

        // then
        assertThat(result).isTrue();
    }


    @DisplayName("getAuthentication(): 토큰 기반으로 인증정보를 가져올 수 있다.")
    @Test
    @Transactional
    void getAuthentication() {
        // given
        String userEmail = "user2@email.com";
        String token = JwtFactory.builder()
                .subject(userEmail)
                .claims(Map.of("auth", "ROLE_USER"))
                .build()
                .createToken(jwtProperties);

        // when
        Authentication authentication = jwtProvider.getAuthentication(token);

        // then
        assertThat(((UserDetails) authentication.getPrincipal()).getUsername()).isEqualTo(userEmail);
    }

    @DisplayName("getUserId(): 토큰으로 유저 ID를 가져올 수 있다.")
    @Test
    @Transactional
    void getUserId() {
        // given
        Long userId = 1L;
        String token = JwtFactory.builder()
                .claims(Map.of("id", userId))
                .build()
                .createToken(jwtProperties);

        // when
        Long userIdByToken = jwtProvider.getUserId(token);

        // then
        assertThat(userIdByToken).isEqualTo(userId);
    }
}