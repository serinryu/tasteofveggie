package com.example.blog.controller;

import com.example.blog.dto.BlogCreateRequestDTO;
import com.example.blog.dto.BlogResponseDTO;
import com.example.blog.dto.BlogUpdateRequestDTO;
import com.example.blog.service.BlogService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.never;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogViewController.class)
@DisplayName("BlogViewController Test")
public class BlogViewControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BlogService blogService;

    // 잠깐 ! 테스트 대상인 컨트롤러를 생성하지 않는 이유 :
    // 컨트롤러는 서버에 url만 입력하면 동작하므로 컨트롤러를 따로 생성하지는 않는 것임.

    @Test
    @WithMockUser // Add this annotation to simulate an authenticated user -> @WithMockUser(value = "john.doe", roles = "ROLE_USER")
    public void findAllTest() throws Exception {
        // given
        List<BlogResponseDTO> blogs = new ArrayList<>();
        blogs.add(new BlogResponseDTO(1L, "writer 1", "title 1", "content 1", LocalDateTime.now(), LocalDateTime.now(), 0));

        Long pageNum = 1L;
        Page<BlogResponseDTO> blogPage = new PageImpl<>(blogs);

        Mockito.when(blogService.findAll(pageNum)).thenReturn(blogPage);

        // When
        mockMvc.perform(get("/blog/list?page={pageNum}", pageNum))
                // Then
                .andExpect(status().isOk())
                .andExpect(view().name("blog/blogList"))
                .andExpect(model().attributeExists("currentPageNum", "endPageNum", "startPageNum", "pageInfo"))
                .andExpect(model().attribute("pageInfo", blogPage));

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
        url.append("/blog/").append("detail/").append(blogId);
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

    @Test
    @WithMockUser
    public void deleteByIdTest() throws Exception {
        // given
        long blogId = 1;
        Mockito.doNothing().when(blogService).deleteById(blogId);

        // when
        ResultActions response = mockMvc.perform(
                post("/blog/delete/"+blogId)
                    .with(csrf()));

        // then
        response.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog/list"))
                .andDo(print());
        Mockito.verify(blogService).deleteById(blogId);
    }

    @Test
    @WithMockUser
    public void insertTest_ValidData() throws Exception {
        // given
        BlogCreateRequestDTO blogCreateRequestDTO = new BlogCreateRequestDTO("writer", "title", "content");

        // when
        ResultActions response = mockMvc.perform(
                post("/blog/create")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .flashAttr("blogCreateRequestDTO", blogCreateRequestDTO)); // Flash attributes are temporary storage and often used for passing data between 'redirects'.

        // then
        response.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog/list"))
                .andDo(print());

        Mockito.verify(blogService).save(blogCreateRequestDTO);
    }

    @Test
    @WithMockUser
    public void insertTest_InvalidData() throws Exception {
        // given
        BlogCreateRequestDTO blogCreateRequestDTO = new BlogCreateRequestDTO(null, null, "content");

        // when
        ResultActions response = mockMvc.perform(
                post("/blog/create")
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .flashAttr("blogCreateRequestDTO", blogCreateRequestDTO));
                //.content(objectMapper.writeValueAsString(blogCreateRequestDTO))); // serializes the blogCreateRequestDTO object into JSON using the ObjectMapper. It simulates sending a JSON payload in the request body.

        // then
        response.andExpect(view().name("redirect:/blog/list"));

        Mockito.verify(blogService, never()).save(blogCreateRequestDTO);
    }


    @Test
    @WithMockUser
    public void updateTest_ValidData() throws Exception {
        // given
        long blogId = 1;
        BlogUpdateRequestDTO blogUpdateRequestDTO = new BlogUpdateRequestDTO("title", "content");

        // when
        ResultActions response = mockMvc.perform(
                post("/blog/update/"+blogId)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .flashAttr("blogUpdateRequestDTO", blogUpdateRequestDTO)); // Flash attributes are temporary storage and often used for passing data between 'redirects'.

        // then
        response.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog/detail/" + blogId))
                .andDo(print());

        Mockito.verify(blogService).update(blogId, blogUpdateRequestDTO);
    }

    @Test
    @WithMockUser
    public void updateTest_InvalidData() throws Exception {
        // given
        long blogId = 1;
        BlogUpdateRequestDTO blogUpdateRequestDTO = new BlogUpdateRequestDTO(null, "content");

        // when
        ResultActions response = mockMvc.perform(
                post("/blog/update/"+blogId)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .flashAttr("blogUpdateRequestDTO", blogUpdateRequestDTO)); // Flash attributes are temporary storage and often used for passing data between 'redirects'.

        // then
        response.andExpect(redirectedUrl("/blog/detail/" + blogId));

        Mockito.verify(blogService, never()).update(blogId, blogUpdateRequestDTO);
    }

}
