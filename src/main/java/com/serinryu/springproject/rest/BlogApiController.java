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

import java.time.LocalDateTime;
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


    /**
     * Retrieve a list of blogs
     */
    @GetMapping("/api/blogs")
    public ResponseEntity<Map<String, Object>> findAllBlogs(
            Authentication authentication,
            @RequestParam(required = false, defaultValue = "1", value = "page") Long pageNum) {

        String username = "Anonymous";
        if (authentication != null && authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            username = user.getUsername();
            log.info("🌈You are logged in as ... : " + user.getUsername());
            log.info(user.getAuthorities());
        }

        Page<BlogResponseDTO> pageInfo = blogService.findAll(pageNum);

        final int PAGE_BTN_NUM = 10; // 한 페이지에 보여야 하는 페이징 버튼 그룹의 개수
        int currentPageNum = pageInfo.getNumber() + 1; // 현재 조회중인 페이지(0부터 셈). 강조 스타일 위해 필요
        int endPageNum = (int) Math.ceil(currentPageNum / (double) PAGE_BTN_NUM) * PAGE_BTN_NUM;  // 현재 조회중인 페이지 그룹의 끝번호
        int startPageNum = endPageNum - PAGE_BTN_NUM + 1; // 현재 조회중인 페이지 그룹의 시작번호
        // 마지막 그룹 번호 보정
        endPageNum = Math.min(endPageNum, pageInfo.getTotalPages());

        Map<String, Object> pageInfoResponse = new HashMap<>();
        pageInfoResponse.put("content", pageInfo.getContent());
        pageInfoResponse.put("totalPages", pageInfo.getTotalPages());
        pageInfoResponse.put("totalElements", pageInfo.getTotalElements());

        Map<String, Object> data = new HashMap<>();
        data.put("currentPageNum", currentPageNum);
        data.put("endPageNum", endPageNum);
        data.put("startPageNum", startPageNum);
        data.put("pageInfo", pageInfoResponse);
        data.put("username", username);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "성공적으로 조회되었습니다.");
        response.put("timestamp", LocalDateTime.now());
        response.put("data", data);

        return ResponseEntity.ok().body(response);
    }

    /**
     * Retrieve a blog by ID
     */
    @GetMapping("/api/blogs/{blogId}")
    public ResponseEntity<Map<String, Object>> findBlog(@PathVariable long blogId) {
        BlogResponseDTO blog = blogService.findById(blogId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "성공적으로 조회하였습니다.");
        response.put("timestamp", LocalDateTime.now());
        response.put("data", blog);

        return ResponseEntity.ok().body(response);
    }

    /**
     * Create new blog
     */
    @PostMapping("/api/blogs")
    public ResponseEntity<Map<String, Object>> addBlog(@Valid @RequestBody BlogCreateRequestDTO blogCreateRequestDTO, BindingResult bindingResult) {

        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("code", 400);
            response.put("message", "Bad Request");
            response.put("errors", bindingResult.getAllErrors());
            response.put("timestamp", LocalDateTime.now());
            return ResponseEntity.badRequest().body(response);
        }

        blogService.save(blogCreateRequestDTO);

        response.put("message", "Blog created successfully.");
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Delete a blog by ID
     */
    @DeleteMapping("/api/blogs/{blogId}")
    public ResponseEntity<Map<String, Object>> deleteBlog(@PathVariable long blogId) {

        blogService.deleteById(blogId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Blog deleted successfully.");
        response.put("timestamp", LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
    }

    /**
     * Update an existing blog by ID
     */
    @PutMapping("/api/blogs/{blogId}")
    public ResponseEntity<Map<String, Object>> updateBlog(@PathVariable long blogId, @Valid @RequestBody BlogUpdateRequestDTO blogUpdateRequestDTO, BindingResult bindingResult) {

        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("message", "Bad Request");
            response.put("timestamp", LocalDateTime.now());
            response.put("data", null);
            response.put("errors", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().body(response);
        }

        blogService.update(blogId, blogUpdateRequestDTO);

        response.put("message", "Blog updated successfully");
        response.put("timestamp", LocalDateTime.now());
        //response.put("data", null);
        return ResponseEntity.ok().body(response);
    }
}
