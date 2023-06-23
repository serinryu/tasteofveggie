package com.example.blog.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc // MVC 테스트는 브라우저를 켜야 원래 테스트가 가능하므로 브라우저를 대체할 객체를 만들어 수행한다.
public class ReplyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    // 컨트롤러
    @BeforeEach
    public void setMockMvc(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context).build();
    }

    @Test
    @Transactional
    void findAllRepliesTest() throws Exception { // mockMvc의 예외를 던져줄 Exception 을 핸들링해야 한다. (perform 메소드 시 필요)
        // given : fixture 설정, 접속 주소 설정
        String replyWriter = "b";
        long replyId = 2;
        String url = "/reply/2/all";  // "reply/{blogId}/all"

        // when : 위에 설정한 url로 접속 후 json 데이터 리턴받아 저장하기.
        // ResultActions 형 자료를 사용해서 json 자료를 받아올 수 있음. 앞에 final 을 붙임으로 불변성 보장 (Optional)
        final ResultActions result = mockMvc.perform(get(url)
                .accept(MediaType.APPLICATION_JSON));

        // then : 리턴받은 json 목록의 0번째 요소의 replyWriter와 replyId가 예상과 일치하는지 확인
        result
                .andExpect(status().isOk()) // 200코드가 출력되었는지 확인
                .andExpect(jsonPath("$[0].replyWriter").value(replyWriter))// 첫 json의 replyWriter 검사
                .andExpect(jsonPath("$[0].replyId").value(replyId));// 첫 json의 replyId 검사

    }
}

