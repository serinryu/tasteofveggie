package com.serinryu.springproject.security;

import com.serinryu.springproject.entity.Role;
import com.serinryu.springproject.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PrincipalDetails implements UserDetails, OAuth2User { // UserDetails 를 상속받아 인증 객체로 사용

    // PrincipalDetails (UserDtalis, OAuth2User를 구현한 객체)
    // 시큐리티가 "/login" 주소 요청이 오면 낚아 채서 로그인을 진행해준다.
    // 로그인을 진행이 완료가 되면 시큐리티 session을 만들어준다.(Security Session(Session안에 특정영역))
    // 해당 세션안에는 Authentication 타입객체가 들어간다.
    // Authentication 은 UserDetails 타입 객체가 들어갈수 있다.
    // UserDetails 안에 use(사용자)를 가지고 있는다.

    private User user;
    private Map<String, Object> attributes;

    // 일반 로그인
    @Builder
    public PrincipalDetails(User user){

        this.user = user;
    }


    // OAuth2 로그인
    @Builder
    public PrincipalDetails(User user, Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    /*
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("user"));
    }
     */

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> grantedAuthorities = new HashSet<>();
        for(Role role : user.getRoles()){
            grantedAuthorities.add(new SimpleGrantedAuthority(role.getRolename()));
        }
        return grantedAuthorities;
    }


    // OAuth2User 인터페이스 메소드
    @Override
    public Map<String, Object> getAttributes(){
        return attributes;
    }

    public Long getId(){
        return user.getId();
    }

    public String getEmail(){
        return user.getEmail();
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // account is not expired
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // account is not locked
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // password is not expired
    }

    @Override
    public boolean isEnabled() {
        return true; // account is enabled (ex. 1년 동안 로그인 안하면 휴먼계정)
    }

    @Override
    public String getName() {
        return null;
    }


}
