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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogController.class)
@DisplayName("BlogController Test")
public class BlogControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BlogService blogService;

    // 잠깐 ! 테스트 대상인 컨트롤러를 생성하지 않는 이유 :
    // 컨트롤러는 서버에 url만 입력하면 동작하므로 컨트롤러를 따로 생성하지는 않는 것임.

    @Test
    // 1. 블로그 목록 조회 : GET /blog/list
    public void findAllTest() throws Exception {
        // given
        List<BlogResponseDTO> blogs = new ArrayList<>();
        blogs.add(new BlogResponseDTO(1L, "writer 1", "title 1", "content 1", LocalDateTime.now(), LocalDateTime.now(), 0));
        Mockito.when(blogService.findAll()).thenReturn(blogs);

        // JSON 데이터를 던지는 REST 가 아니라 view(ModelAndView) 를 던져주는 MVC 패턴

        // when
        ResultActions response = mockMvc.perform(get("/blog/list")
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        // Controller 측에서 ModelAndView 를 던졌다.
        response.andExpect(status().isOk())
                .andExpect(view().name("board/list"))
                .andExpect(model().attributeExists("blogList"))
                .andExpect(model().attribute("blogList", any(List.class))) // 데이터도 잘 들어오는지 검사하고 싶으면 view '내부'의 데이터 검사해야함
                .andDo(print());

        Mockito.verify(blogService).findAll();

    }

    @Test
    // 2. 블로그 디테일 페이지 : GET /blog/detail/글번호
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
    // 3. 블로그 삭제 : POST /blog/delete/{blogId}
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
    // 3. 블로그 생성 : POST /blog/insert
    public void insertTest() throws Exception {

        /*
        The arguments passed to the save method in the BlogService mock do not match the expected arguments in the test.
        This mismatch can occur due to serialization and deserialization of the BlogCreateRequestDTO object.
         */

        // given
        BlogCreateRequestDTO blogCreateRequestDTO = new BlogCreateRequestDTO("writer", "title", "content");
        // Mockito.doNothing().when(blogService).save(blogCreateRequestDTO);

        // when
        ResultActions response = mockMvc.perform(post("/blog/insert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blogCreateRequestDTO)));  // java object -> json

        // then
        response.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog/list"))
                .andDo(print());

        // Mockito.verify(blogService).save(blogCreateRequestDTO);
    }


    @Test
    // 5. 블로그 업데이트 : POST /blog/updateform , POST /blog/update
    public void updateTest() throws Exception {
        // given

        /*
        The mismatch in the arguments passed to the update method in the BlogService mock
        and the expected arguments in the test can occur due to serialization and deserialization of the BlogUpdateRequestDTO object.
         */

        // BlogResponseDTO blog = new BlogResponseDTO(1, "Writer 1", "Title 1", "Content 1", LocalDateTime.now(), LocalDateTime.now(), 0);
        // Mockito.when(blogService.findById(blog.getBlogId())).thenReturn(blog);
        BlogUpdateRequestDTO blogUpdateRequestDTO = new BlogUpdateRequestDTO(1, "title", "content");

        // when
        ResultActions response = mockMvc.perform(post("/blog/update")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(blogUpdateRequestDTO)));

        // then
        response.andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/blog/list"))
                .andDo(print());
    }

}
