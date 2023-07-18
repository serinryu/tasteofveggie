package com.example.blog.controller;

import com.example.blog.dto.BlogCreateRequestDTO;
import com.example.blog.dto.BlogResponseDTO;
import com.example.blog.dto.BlogUpdateRequestDTO;
import com.example.blog.service.BlogService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/blog1")
@Log4j2
public class BlogRestController {
    BlogService blogService;
    @Autowired
    public BlogRestController(BlogService blogService){
        this.blogService = blogService;
    }

    // 1. 블로그 목록 조회 : GET /blog/list
    @GetMapping("/list")
    public ResponseEntity<Page<BlogResponseDTO>> list(@RequestParam(required = false, defaultValue = "1", value = "page") Long pageNum) {
        Page<BlogResponseDTO> pageInfo = blogService.findAll(pageNum);

        final int PAGE_BTN_NUM = 10;
        int currentPageNum = pageInfo.getNumber() + 1;
        int endPageNum = (int) Math.ceil(currentPageNum / (double) PAGE_BTN_NUM) * PAGE_BTN_NUM;
        int startPageNum = endPageNum - PAGE_BTN_NUM + 1;
        endPageNum = Math.min(endPageNum, pageInfo.getTotalPages());

        HttpHeaders headers = new HttpHeaders();
        headers.add("current-page", String.valueOf(currentPageNum));
        headers.add("end-page", String.valueOf(endPageNum));
        headers.add("start-page", String.valueOf(startPageNum));

        return new ResponseEntity<>(pageInfo, headers, HttpStatus.OK);
    }

    // 2. 블로그 디테일 페이지 : GET /blog/detail/글번호
    @GetMapping("/detail/{blogId}")
    public ResponseEntity<BlogResponseDTO> detail(@PathVariable long blogId) {
        BlogResponseDTO blogFindByIdDTO = blogService.findById(blogId);
        return new ResponseEntity<>(blogFindByIdDTO, HttpStatus.OK);
    }

    // 3. 블로그 삭제 : POST /blog/delete/{blogId}
    @DeleteMapping("/delete/{blogId}")
    public ResponseEntity<Void> delete(@PathVariable long blogId) {
        blogService.deleteById(blogId);
        return ResponseEntity.noContent().build();
    }

    // 4. 블로그 생성 : POST /blog/insert
    @PostMapping("/insert")
    public ResponseEntity<Void> insert(@Valid @RequestBody BlogCreateRequestDTO blogCreateRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
        /*
        에러 메세지 추가 필요
        */
            System.out.println(bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }
        blogService.save(blogCreateRequestDTO);
        return ResponseEntity.ok().build();
    }

    // 5. 블로그 업데이트 : POST /blog/update/{blogId}
    @PostMapping("/update/{blogId}")
    public ResponseEntity<Void> update(@PathVariable long blogId, @Valid @RequestBody BlogUpdateRequestDTO blogUpdateRequestDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
        /*
        에러 메세지 추가 필요
        */
            return ResponseEntity.badRequest().build();
        }
        blogService.update(blogId, blogUpdateRequestDTO);
        return ResponseEntity.ok().build();
    }

}
