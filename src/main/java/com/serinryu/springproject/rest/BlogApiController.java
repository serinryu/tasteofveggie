package com.serinryu.springproject.rest;

import com.serinryu.springproject.dto.BlogCreateRequestDTO;
import com.serinryu.springproject.dto.BlogResponseDTO;
import com.serinryu.springproject.dto.BlogUpdateRequestDTO;
import org.springframework.security.core.userdetails.User;
import com.serinryu.springproject.service.BlogService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
public class BlogApiController {
    BlogService blogService;
    @Autowired
    public BlogApiController(BlogService blogService){
        this.blogService = blogService;
    }

    private static final Logger logger = LogManager.getLogger(BlogApiController.class);


    @GetMapping("/api/blogs")
    public ResponseEntity<Map<String, Object>> findAllBlogs(
            Authentication authentication,
            @RequestParam(required = false, defaultValue = "1", value = "page") Long pageNum) {

        String username = "Anonymous";
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            username = user.getUsername();
            log.info("🌈You are logged in as ... : " + user.getUsername());
        }

        Page<BlogResponseDTO> pageInfo = blogService.findAll(pageNum);

        final int PAGE_BTN_NUM = 10; // 한 페이지에 보여야 하는 페이징 버튼 그룹의 개수
        int currentPageNum = pageInfo.getNumber() + 1; // 현재 조회중인 페이지(0부터 셈). 강조 스타일 위해 필요
        int endPageNum = (int) Math.ceil(currentPageNum / (double) PAGE_BTN_NUM) * PAGE_BTN_NUM;  // 현재 조회중인 페이지 그룹의 끝번호
        int startPageNum = endPageNum - PAGE_BTN_NUM + 1; // 현재 조회중인 페이지 그룹의 시작번호

        // 마지막 그룹 번호 보정
        endPageNum = Math.min(endPageNum, pageInfo.getTotalPages());

        Map<String, Object> response = new HashMap<>();
        response.put("currentPageNum", currentPageNum);
        response.put("endPageNum", endPageNum);
        response.put("startPageNum", startPageNum);
        response.put("pageInfo", pageInfo);
        response.put("username", username);

        return ResponseEntity.ok().body(response);
    }

    @GetMapping("/api/blogs/{blogId}")
    public ResponseEntity<BlogResponseDTO> findBlog(@PathVariable long blogId) {
        BlogResponseDTO blog = blogService.findById(blogId);
        return ResponseEntity.ok().body(blog);
    }

    @GetMapping("/api/blogs/new")
    public ResponseEntity<BlogResponseDTO> getNewBlogForm(@RequestParam(required = false, value = "id") Long blogId) {
        BlogResponseDTO blog = blogService.findById(blogId);
        return ResponseEntity.ok().body(blog);
    }

    @DeleteMapping("/api/blogs/{blogId}")
    public ResponseEntity<String> deleteBlog(@PathVariable long blogId) {

        blogService.deleteById(blogId);

        logger.info("Blog deleted successfully.");
        return ResponseEntity.ok().body("Success");
    }

    @PostMapping("/api/blogs")
    public ResponseEntity<String> addBlog(@Valid @RequestBody BlogCreateRequestDTO blogCreateRequestDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            logger.error("Validation errors: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }

        blogService.save(blogCreateRequestDTO);

        logger.info("Blog created successfully.");
        return ResponseEntity.status(HttpStatus.CREATED).body("Success");
    }

    @PutMapping("/api/blogs/{blogId}")
    public ResponseEntity<String> updateBlog(@PathVariable long blogId, @Valid @RequestBody BlogUpdateRequestDTO blogUpdateRequestDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            logger.error("Validation errors: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }

        blogService.update(blogId, blogUpdateRequestDTO);
        return ResponseEntity.ok().body("Success");
    }
}
