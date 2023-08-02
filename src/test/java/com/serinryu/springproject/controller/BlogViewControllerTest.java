package com.serinryu.springproject.controller;

import com.serinryu.springproject.dto.BlogResponseDTO;
import com.serinryu.springproject.service.BlogService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithAnonymousUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogViewController.class)
@DisplayName("BlogViewController Test")
public class BlogViewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BlogService blogService;

    // 잠깐 ! 테스트 대상인 컨트롤러를 생성하지 않는 이유 :
    // 컨트롤러는 서버에 url만 입력하면 동작하므로 컨트롤러를 따로 생성하지는 않는 것임.

    @Test
    @WithMockUser(username = "testuser", password = "password", roles = "USER")
    public void findAllTest() throws Exception {
        // given
        List<BlogResponseDTO> blogs = new ArrayList<>();
        blogs.add(new BlogResponseDTO(1L, "writer 1", "title 1", "content 1", LocalDateTime.now(), LocalDateTime.now(), 0));

        Long pageNum = 1L;
        Page<BlogResponseDTO> blogPage = new PageImpl<>(blogs);

        Mockito.when(blogService.findAll(pageNum)).thenReturn(blogPage);

        // When
        mockMvc.perform(get("/blogs?page={pageNum}", pageNum))
                // Then
            .andExpect(status().isOk())
            .andExpect(view().name("blog/blogList"))
            .andExpect(model().attributeExists("currentPageNum", "endPageNum", "startPageNum", "pageInfo"))
            .andExpect(model().attribute("pageInfo", blogPage))
            .andExpect(model().attribute("username", "testuser"));

        Mockito.verify(blogService, Mockito.times(1)).findAll(pageNum);
    }

    @Test
    @WithMockUser
    public void findByIdTest() throws Exception {
        // given
        long blogId = 1;
        BlogResponseDTO blog = new BlogResponseDTO(blogId, "Writer 1", "Title 1", "Content 1", LocalDateTime.now(), LocalDateTime.now(), 0);
        Mockito.when(blogService.findById(blogId)).thenReturn(blog);

        // when
        StringBuilder url = new StringBuilder();
        url.append("/blogs/").append(blogId);
        ResultActions response = mockMvc.perform(get(String.valueOf(url))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(view().name("blog/blogDetail"))
                .andExpect(model().attributeExists("blog"))
                .andExpect(model().attribute("blog", any(BlogResponseDTO.class)))
                .andDo(print());

        Mockito.verify(blogService).findById(blogId);
    }

}
