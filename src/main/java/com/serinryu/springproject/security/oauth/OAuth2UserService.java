package com.serinryu.springproject.security.oauth;

import com.serinryu.springproject.entity.Role;
import com.serinryu.springproject.repository.RoleRepository;
import com.serinryu.springproject.security.PrincipalDetails;
import com.serinryu.springproject.entity.User;
import com.serinryu.springproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@RequiredArgsConstructor
@Service
public class OAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        //"registraionId" 로 어떤 OAuth 로 로그인 했는지 확인 가능(google,naver등)
        System.out.println("getClientRegistration: "+userRequest.getClientRegistration());
        System.out.println("getAccessToken: "+userRequest.getAccessToken().getTokenValue());
        System.out.println("getAttributes: "+ super.loadUser(userRequest).getAttributes());

        /**
         *  OAuth 로그인 회원 가입
         */
        OAuth2User oAuth2User = super.loadUser(userRequest); // 요청을 바탕으로 유저 정보를 담은 객체 반환

        User user = saveOrUpdate(oAuth2User);

        return new PrincipalDetails(user, oAuth2User.getAttributes());
    }

    private User saveOrUpdate(OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String name = (String) attributes.get("name");

        // 기본적으로 회원가입한 사람은 다 USER
        Set<Role> rolesSet = new HashSet<>();
        rolesSet.add(roleRepository.findByRolename("ROLE_USER").get());

        User user = userRepository.findByEmail(email)
                .map(entity -> entity.update(name)) // 같은 이메일로 유저가 있으면 업데이트
                // 처음 서비스를 사용한 회원일 경우 (유저가 없으면 유저 생성)
                .orElse(User.builder()
                    .email(email)
                    .nickname(name)
                    .roles(rolesSet)
                    .build());

        return userRepository.save(user);
    }
}
