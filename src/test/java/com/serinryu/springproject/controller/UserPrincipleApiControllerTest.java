package com.serinryu.springproject.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


/*
Spring Security 프레임워크를 통해 생성된 필터 체인이 정상적으로 동작하는지 테스트합니다.
 */
@SpringBootTest
@AutoConfigureMockMvc
class UserPrincipleApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void givenWithoutToken_whenCallLogin_thenIsOk() throws Exception {
        mockMvc.perform(post("/login"))
                .andExpect(status().isOk());
    }

    @Test
    public void givenWithoutToken_whenCallNotExistsPath_thenIsForbidden() throws Exception {

        mockMvc.perform(post("/something-other"))
                .andExpect(status().isForbidden());
    }
}