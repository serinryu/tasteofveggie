package com.serinryu.springproject.security;

import com.serinryu.springproject.security.jwt.JwtAuthenticationFailureHandler;
import com.serinryu.springproject.security.jwt.JwtAuthenticationSuccessHandler;
import com.serinryu.springproject.security.jwt.JwtProvider;
import com.serinryu.springproject.security.oauth.OAuth2AuthorizationRequestBasedOnCookieRepository;
import com.serinryu.springproject.security.oauth.OAuth2SuccessHandler;
import com.serinryu.springproject.security.oauth.OAuth2UserService;
import com.serinryu.springproject.repository.RefreshTokenRepository;
import com.serinryu.springproject.security.jwt.TokenService;
import com.serinryu.springproject.service.UserDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenService tokenService;
    private final UserDetailService userDetailService;

    private final OAuth2UserService oAuth2UserService;

    // 스프링 시큐리티 기능 비활성화 (모든 곳에 인증, 인가 서비스를 적용할 필요 없음. static 디렉토리의 파일들은 항상 인증 무시)
    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring()
                //Spring Security 의존성을 추가하면, 스프링 부트는 그 스프링 시큐리티 자동설정을 적용해줘서, 모든 요청에 대해서 인증을 필요로 하게 되므로, 직접 설정
                .requestMatchers("/static/**"); // src/main/java/resources/static/ 으로, 추후 설정할 정적자원 저장 경로에 보안을 풀었음.
                //.dispatcherTypeMatchers(DispatcherType.FORWARD); // MVC방식에서 뷰단 파일을 로딩하는것을 보안범위에서 해제.
    }

    // 특정 HTTP 요청에 대한 웹 기반 보안 구성
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // 헤더에 토큰으로 "basic "으로 된 토큰을 사용하는 경우 -> httpBasic() / 사용하지 않으면 "BasicAu~"가 작동안하는데 우리는 JWT 토큰을 사용하니 커스텀해서 등록해주기
            .httpBasic(httpBasic -> httpBasic.disable())

            // disable CSRF (JWT 를 사용하므로 disable)
            .csrf(csrf -> csrf.disable())

            // 시큐리티는 기본적으로 세션을 사용. 여기서는 세션을 사용하지 않기 때문에 세션 설정을 Stateless 로 설정
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Set permissions on endpoints
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/api/blogs", "/api/token").permitAll() // Allow public access to the token endpoint
                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                .requestMatchers("/api/**").authenticated() // Secure all other API endpoints
                .anyRequest().permitAll()
            )

            // 커스텀 필터 추가 -> jwtAuthenticationFilter will be executed before the UsernamePasswordAuthenticationFilter
            .addFilterBefore(jwtAuthenticationFilter(),
                    UsernamePasswordAuthenticationFilter.class)

            // Form based Auth  -> Spring Security 제공. POST /login 해서 로직 작성할 필요 없음
            .formLogin(form -> form
                .loginPage("/login") // HTML Form 을 통해 POST /login
                .successHandler(new JwtAuthenticationSuccessHandler(
                        tokenService,
                        userDetailService
                ))
                .failureHandler(new JwtAuthenticationFailureHandler())
            )

            // OAuth2
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .authorizationEndpoint(authorization -> authorization
                        .authorizationRequestRepository(oAuth2AuthorizationRequestBasedOnCookieRepository())
                )
                .userInfoEndpoint(userInfo -> userInfo
                        .userService(oAuth2UserService) // 구글 로그인이 완료된(구글회원) 뒤의 후처리가 필요함
                )
                .successHandler(oAuth2SuccessHandler())
            )

            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID", "refresh_token")
                // access_token 토큰의 경우 localStorage 에 저장되어 있으므로 클라이언트 측에서 JavaScript로 삭제해야함. (
            )

            // /api/** 로 들어오는 url 일 경우 401 상태 코드 반환하도록
            .exceptionHandling(exception -> exception
                .defaultAuthenticationEntryPointFor(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED),
                        new AntPathRequestMatcher("/api/**"))
            );

        return http.build();
    }


    // 인증 관리자 관련 설정
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, UserDetailService userDetailService) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder
                .userDetailsService(userDetailService)
                .passwordEncoder(bCryptPasswordEncoder);
        return authenticationManagerBuilder.build();
    }


    @Bean
    public OAuth2SuccessHandler oAuth2SuccessHandler() {
        return new OAuth2SuccessHandler(tokenService,
                oAuth2AuthorizationRequestBasedOnCookieRepository(),
                userDetailService
        );
    }

    @Bean
    public OAuth2AuthorizationRequestBasedOnCookieRepository oAuth2AuthorizationRequestBasedOnCookieRepository() {
        return new OAuth2AuthorizationRequestBasedOnCookieRepository();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtProvider);
    }

    // 패스워드 인코더로 사용할 빈 등록
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
}

