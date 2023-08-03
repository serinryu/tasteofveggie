package com.serinryu.springproject.controller;

import com.serinryu.springproject.dto.BlogCreateRequestDTO;
import com.serinryu.springproject.dto.BlogResponseDTO;
import com.serinryu.springproject.dto.BlogUpdateRequestDTO;
import com.serinryu.springproject.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;

import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
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
    @WithMockUser(username = "testuser", password = "password", roles = "USER")
    public void testFindAllBlogs() throws Exception {
        // Mocking blogService.findAll(pageNum)
        long totalElements = 15;
        int pageSize = 5;
        int currentPage = 1;
        Page<BlogResponseDTO> mockPage = new PageImpl<>(Collections.emptyList(),
                PageRequest.of(currentPage - 1, pageSize, Sort.by(Sort.Direction.DESC, "id")), totalElements);
        Mockito.when(blogService.findAll(Mockito.any(Long.class))).thenReturn(mockPage);

        mockMvc.perform(get("/api/blogs")
                        .param("page", "1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(jsonPath("$.currentPageNum").value(currentPage))
                .andExpect(jsonPath("$.endPageNum").value(3))
                .andExpect(jsonPath("$.startPageNum").value(1))
                .andExpect(jsonPath("$.pageInfo").exists())
                .andExpect(jsonPath("$.username").value("testuser"));
    }

    @Test
    @WithMockUser(username = "testuser@example")
    public void DeleteTest_Success() throws Exception {
        // given
        long blogId = 1L;
        String username = "testuser@example";
        BlogResponseDTO blog = new BlogResponseDTO(blogId, username, "original title", "original content", null, null, 0);
        Mockito.when(blogService.findById(blogId)).thenReturn(blog);

        // when
        mockMvc.perform(delete("/api/blogs/{blogId}", blogId)
                    .with(csrf()))
                .andExpect(status().isOk());
        // then
        Mockito.verify(blogService).deleteById(blogId);
    }

    @Test
    public void DeleteTest_Unauthorized() throws Exception {
        long blogIdToDelete = 1L;

        mockMvc.perform(delete("/api/blogs/{blogId}", blogIdToDelete)
                .with(csrf()))
                //.andExpect(status().isUnauthorized());
                .andExpect(status().is3xxRedirection());

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
    @WithMockUser(username = "testuser@example")
    public void updateTest_Success() throws Exception {
        // given
        long blogId = 1;
        String username = "testuser@example";
        BlogUpdateRequestDTO blogUpdateRequestDTO = new BlogUpdateRequestDTO();
        blogUpdateRequestDTO.setBlogTitle("title");
        blogUpdateRequestDTO.setBlogContent("content");

        BlogResponseDTO blog = new BlogResponseDTO(blogId, username, "original title", "original content", null, null, 0);

        Mockito.when(blogService.findById(blogId)).thenReturn(blog);

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
