package com.serinryu.springproject.controller;

import com.serinryu.springproject.dto.BlogCreateRequestDTO;
import com.serinryu.springproject.dto.BlogUpdateRequestDTO;
import com.serinryu.springproject.service.BlogService;
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

import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogApiController.class)
@DisplayName("BlogApiController Test")
public class BlogApiControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BlogService blogService;

    // 잠깐 ! 테스트 대상인 컨트롤러를 생성하지 않는 이유 :
    // 컨트롤러는 서버에 url만 입력하면 동작하므로 컨트롤러를 따로 생성하지는 않는 것임.

    @Test
    @WithMockUser
    public void DeleteTest_Success() throws Exception {
        long blogId = 1L;
        Mockito.doNothing().when(blogService).deleteById(blogId); // void 함수를 검증할 수 있는 방법은 이 함수가 호츌되었는지

        mockMvc.perform(delete("/api/blogs/{blogId}", blogId)
                    .with(csrf()))
                .andExpect(status().isOk());

        Mockito.verify(blogService).deleteById(blogId);
    }

    @Test
    public void DeleteTest_Unauthorized() throws Exception {
        long blogIdToDelete = 1L;

        mockMvc.perform(delete("/api/blogs/{blogId}", blogIdToDelete)
                        .with(csrf()))
                .andExpect(status().isUnauthorized());

        Mockito.verifyNoInteractions(blogService);
    }


    @Test
    @WithMockUser(username = "testuser@example")
    public void addTest_Success() throws Exception {
        // given
        BlogCreateRequestDTO blogCreateRequestDTO = new BlogCreateRequestDTO();
        blogCreateRequestDTO.setBlogTitle("Test Blog");
        blogCreateRequestDTO.setBlogContent("This is a test blog content.");

        // when
        ResultActions response = mockMvc.perform(
                post("/api/blogs")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString((blogCreateRequestDTO))));
                    //.flashAttr("blogCreateRequestDTO", blogCreateRequestDTO)); // Flash attributes are temporary storage and often used for passing data between 'redirects'.

        // then
        response.andExpect(status().isCreated())
                .andDo(print());

        //Mockito.verify(blogService).save(new BlogCreateRequestDTO("testuser@example", "Test Blog", "This is a test blog content."));
    }

    @Test
    @WithMockUser
    public void updateTest_Success() throws Exception {
        // given
        long blogId = 1;
        BlogUpdateRequestDTO blogUpdateRequestDTO = new BlogUpdateRequestDTO("title", "content");

        // when
        ResultActions response = mockMvc.perform(
                put("/api/blogs/"+blogId)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString((blogUpdateRequestDTO))));
                    //.flashAttr("blogUpdateRequestDTO", blogUpdateRequestDTO)); // Flash attributes are temporary storage and often used for passing data between 'redirects'.

        // then
        response.andExpect(status().isOk())
                .andDo(print());

        //Mockito.verify(blogService).update(blogId, blogUpdateRequestDTO);
    }

    @Test
    @WithMockUser
    public void updateTest_InvalidData() throws Exception {
        // given
        long blogId = 1;
        BlogUpdateRequestDTO blogUpdateRequestDTO = new BlogUpdateRequestDTO(null, "content");

        // when
        ResultActions response = mockMvc.perform(
                put("/api/blogs/"+blogId)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString((blogUpdateRequestDTO))));
                    //.flashAttr("blogUpdateRequestDTO", blogUpdateRequestDTO)); // Flash attributes are temporary storage and often used for passing data between 'redirects'.

        // then
        response.andExpect(status().isBadRequest());

        Mockito.verify(blogService, never()).update(blogId, blogUpdateRequestDTO);
    }

}
