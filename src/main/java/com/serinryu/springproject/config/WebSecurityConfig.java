package com.serinryu.springproject.config;

import com.serinryu.springproject.service.UserDetailService;
import jakarta.servlet.DispatcherType;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@RequiredArgsConstructor
@Configuration
public class WebSecurityConfig {

    private final UserDetailService userService;

    // 스프링 시큐리티 기능 비활성화 (모든 곳에 인증, 인가 서비스를 적용할 필요 없음. static 디렉토리의 파일들은 항상 인증 무시)
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                //Spring Security 의존성을 추가하면, 스프링 부트는 그 스프링 시큐리티 자동설정을 적용해줘서, 모든 요청에 대해서 인증을 필요로 하게 되므로, 직접 설정
                .requestMatchers("/static/**") // src/main/java/resources/static/ 으로, 추후 설정할 정적자원 저장 경로에 보안을 풀었음.
                .dispatcherTypeMatchers(DispatcherType.FORWARD); // MVC방식에서 뷰단 파일을 로딩하는것을 보안범위에서 해제.
                // 그 외의 페이지들은 인증된 사용자만 들어갈 수 있는 페이지이므로 /login 으로 리다이렉트됨
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 인증, 인가 설정
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login", "/signup", "/user")
                .permitAll() // 위 3개의 페이지는 별도 인증 없이 접근 가능
                .anyRequest()
                .authenticated()
            )
            // 폼 기반 로그인 설정
            .formLogin(form -> form
                .loginPage("/login") // POST /login
                .defaultSuccessUrl("/blog/list")
                // .failureUrl("/login?error=true")
            )
            // 로그아웃 설정
            .logout(logout -> logout
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
            )
            .csrf(csrf -> csrf
                .disable());
        return http.build();
    }

    // 인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userService)
                .passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }

    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}