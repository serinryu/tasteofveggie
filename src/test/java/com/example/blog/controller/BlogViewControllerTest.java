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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.any;
import static org.mockito.Mockito.never;
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
    public void findAllTest() throws Exception {
        // given
        List<BlogResponseDTO> blogs = new ArrayList<>();
        blogs.add(new BlogResponseDTO(1L, "writer 1", "title 1", "content 1", LocalDateTime.now(), LocalDateTime.now(), 0));
        Mockito.when(blogService.findAll()).thenReturn(blogs);

        // when
        // JSON 데이터를 던지는 REST 가 아니라 view(ModelAndView) 를 던져주는 MVC 패턴
        ResultActions response = mockMvc.perform(get("/blog/list")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        response.andExpect(status().isOk())
                .andExpect(view().name("board/list"))
                .andExpect(model().attributeExists("blogList"))
                .andExpect(model().attribute("blogList", any(List.class))) // 데이터도 잘 들어오는지 검사하고 싶으면 view '내부'의 데이터 검사해야함
                .andDo(print());

        Mockito.verify(blogService).findAll();

    }

    @Test
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
                .andExpect(view().name("blog/detail"))
                .andExpect(model().attributeExists("blog"))
                .andExpect(model().attribute("blog", any(BlogResponseDTO.class)))
                .andDo(print());

        Mockito.verify(blogService).findById(blogId);
    }

    @Test
    public void deleteByIdTest() throws Exception {
        // given
        long blogId = 2;
        Mockito.doNothing().when(blogService).deleteById(blogId);

        // when
        ResultActions response = mockMvc.perform(post("/blog/delete/"+blogId));

        // then
        response.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog/list"))
                .andDo(print());
        Mockito.verify(blogService).deleteById(blogId);
    }

    @Test
    public void insertTest_ValidData() throws Exception {
        // given
        BlogCreateRequestDTO blogCreateRequestDTO = new BlogCreateRequestDTO("writer", "title", "content");

        // when
        ResultActions response = mockMvc.perform(post("/blog/create")
                .contentType(MediaType.APPLICATION_JSON)
                .flashAttr("blogCreateRequestDTO", blogCreateRequestDTO)); // Flash attributes are temporary storage and often used for passing data between 'redirects'.

        // then
        response.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog/list"))
                .andDo(print());

        Mockito.verify(blogService).save(blogCreateRequestDTO);
    }

    @Test
    public void insertTest_InvalidData() throws Exception {
        // given
        BlogCreateRequestDTO blogCreateRequestDTO = new BlogCreateRequestDTO("writer", null, "content");

        // when
        ResultActions response = mockMvc.perform(post("/blog/create")
                .contentType(MediaType.APPLICATION_JSON)
                .flashAttr("blogCreateRequestDTO", blogCreateRequestDTO));
                //.content(objectMapper.writeValueAsString(blogCreateRequestDTO))); // serializes the blogCreateRequestDTO object into JSON using the ObjectMapper. It simulates sending a JSON payload in the request body.

        // then
        response.andExpect(view().name("blog/blog-form"));

        Mockito.verify(blogService, never()).save(blogCreateRequestDTO);
    }


    @Test
    public void updateTest_ValidData() throws Exception {
        // given
        long blogId = 1;
        BlogUpdateRequestDTO blogUpdateRequestDTO = new BlogUpdateRequestDTO(blogId, "title", "content");

        // when
        ResultActions response = mockMvc.perform(put("/blog/update/"+blogId)
                .contentType(MediaType.APPLICATION_JSON)
                .flashAttr("blogUpdateRequestDTO", blogUpdateRequestDTO)); // Flash attributes are temporary storage and often used for passing data between 'redirects'.

        // then
        response.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog/detail/" + blogUpdateRequestDTO.getBlogId()))
                .andDo(print());

        Mockito.verify(blogService).update(blogUpdateRequestDTO);
    }

    @Test
    public void updateTest_InvalidData() throws Exception {
        // given
        long blogId = 1;
        BlogUpdateRequestDTO blogUpdateRequestDTO = new BlogUpdateRequestDTO(blogId, null, "content");

        // when
        ResultActions response = mockMvc.perform(put("/blog/update/"+blogId)
                .contentType(MediaType.APPLICATION_JSON)
                .flashAttr("blogUpdateRequestDTO", blogUpdateRequestDTO)); // Flash attributes are temporary storage and often used for passing data between 'redirects'.

        // then
        response.andExpect(redirectedUrl("/blog/detail/" + blogId));

        Mockito.verify(blogService, never()).update(blogUpdateRequestDTO);
    }

}
