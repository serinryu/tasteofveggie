package com.example.blog.controller;

import com.example.blog.dto.ReplyCreateRequestDTO;
import com.example.blog.dto.ReplyResponseDTO;
import com.example.blog.dto.ReplyUpdateRequestDTO;
import com.example.blog.service.ReplyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReplyApiController.class)
@DisplayName("ReplyApiController Test")
public class ReplyApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReplyService replyService;

    // 잠깐 ! 테스트 대상인 컨트롤러를 생성하지 않는 이유 :
    // 컨트롤러는 서버에 url만 입력하면 동작하므로 컨트롤러를 따로 생성하지는 않는 것임.

    @Test
    @WithMockUser
    public void findAllRepliesTest() throws Exception {
        // given
        long blogId = 1;
        List<ReplyResponseDTO> replies = new ArrayList<>();
        replies.add(new ReplyResponseDTO(1, "writer 1", "content 1", LocalDateTime.now(), LocalDateTime.now()));
        Mockito.when(replyService.findAllByBlogId(blogId)).thenReturn(replies);

        // when
        StringBuilder url = new StringBuilder();
        url.append("/reply/").append(blogId).append("/all");
        ResultActions response = mockMvc.perform(get(String.valueOf(url))
                .accept(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
               .andExpect(jsonPath("$[0].replyWriter").value("writer 1")) // json의 첫번째 요소의 replyWriter 검사
                .andExpect(jsonPath("$[0].replyContent").value("content 1")) // json의 첫번째 요소의 replyWriter 검사
               .andExpect(jsonPath("$[0].replyId").value(1)) // json의 첫번째 요소의 replyId 검사
                .andDo(print());

        Mockito.verify(replyService).findAllByBlogId(blogId);
    }

    @Test
    @WithMockUser
    public void findByReplyIdTest_Success() throws Exception {
        // given
        long replyId = 1;
        ReplyResponseDTO replyResponseDTO = new ReplyResponseDTO(1, "writer 1", "content 1", LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(replyService.findByReplyId(replyId)).thenReturn(replyResponseDTO);

        // when
        StringBuilder url = new StringBuilder();
        url.append("/reply/").append(replyId);
        ResultActions response = mockMvc.perform(get(String.valueOf(url))
                .accept(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.replyWriter").value("writer 1"))
                .andExpect(jsonPath("$.replyContent").value("content 1"))
                .andExpect(jsonPath("$.replyId").value(1))
                .andDo(print());

        Mockito.verify(replyService).findByReplyId(replyId);

    }

    @Test
    @WithMockUser
    public void addReplyTest_ValidData() throws Exception {
        // given
        ReplyCreateRequestDTO replyCreateRequestDTO = new ReplyCreateRequestDTO(1, "writer 1", "content 1");
        Mockito.doNothing().when(replyService).save(replyCreateRequestDTO);

        // when
        ResultActions response = mockMvc.perform(post("/reply")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyCreateRequestDTO)));  // java object -> json (serialization)

        // then
        response.andExpect(status().isOk())
                        .andDo(print());

        Mockito.verify(replyService).save(argThat(reply -> reply.getReplyContent().equals("content 1")));
    }

    @Test
    @WithMockUser
    public void addReplyTest_InvalidData() throws Exception {
        // given
        ReplyCreateRequestDTO replyCreateRequestDTO = new ReplyCreateRequestDTO(1, null, "content 1");

        // when
        ResultActions response = mockMvc.perform(post("/reply")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyCreateRequestDTO)));  // java object -> json

        // then
        response.andExpect(status().isBadRequest());

        Mockito.verify(replyService, never()).save(any());
    }

    @Test
    @WithMockUser
    public void deleteByReplyIdTest() throws Exception {
        // given
        long replyId = 2;
        Mockito.doNothing().when(replyService).deleteByReplyId(replyId);

        // when
        ResultActions response = mockMvc.perform(delete("/reply/{replyId}", replyId)
                .with(csrf()));

        // then
        response.andExpect(status().isOk())
                .andDo(print());
        Mockito.verify(replyService).deleteByReplyId(replyId);
    }

    @Test
    @WithMockUser
    public void updateReplyTest_ValidData() throws Exception {
        // given
        long replyId = 1L;
        ReplyUpdateRequestDTO replyUpdateRequestDTO = new ReplyUpdateRequestDTO("updated content");

        // when
        ResultActions response = mockMvc.perform(put("/reply/{replyId}", replyId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyUpdateRequestDTO)));  // java object -> json

        // then
        response.andExpect(status().isOk())
                .andDo(print());

        //Mockito.verify(replyService).update(replyId, replyUpdateRequestDTO);

    }

    @Test
    @WithMockUser
    public void updateReplyTest_InvalidData() throws Exception {
        // given
        long replyId = 1L;
        ReplyUpdateRequestDTO replyUpdateRequestDTO = new ReplyUpdateRequestDTO((String) null);

        // when
        ResultActions response = mockMvc.perform(put("/reply/{replyId}", replyId)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(replyUpdateRequestDTO)));  // java object -> json

        // then
        response.andExpect(status().isBadRequest())
                .andDo(print());

        //Mockito.verify(replyService, never()).update(replyId, replyUpdateRequestDTO);
    }

}

