package com.serinryu.springproject.service;

import com.serinryu.springproject.dto.SignUpRequestDTO;
import com.serinryu.springproject.entity.Role;
import com.serinryu.springproject.entity.User;
import com.serinryu.springproject.repository.RoleRepository;
import com.serinryu.springproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Service
@Log4j2
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Long save(SignUpRequestDTO signUpRequestDTO) {

        signUpRequestDTO.setPassword(bCryptPasswordEncoder.encode(signUpRequestDTO.getPassword()));

        // 기본적으로 회원가입한 사람은 다 USER
        Set<Role> rolesSet = new HashSet<>();
        rolesSet.add(roleRepository.findByRolename("ROLE_USER").get());
        User user = signUpRequestDTO.toEntity(rolesSet);

        userRepository.save(user);
        return user.getId();
    }
}
