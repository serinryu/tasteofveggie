package com.example.blog.controller;

import com.example.blog.dto.BlogResponseDTO;
import com.example.blog.dto.BlogCreateRequestDTO;
import com.example.blog.dto.BlogUpdateRequestDTO;
import com.example.blog.service.BlogService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/blog")
@Log4j2
public class BlogViewController {
    BlogService blogService;

    @Autowired // 생성자 주입
    public BlogViewController(BlogService blogService){
        this.blogService = blogService;
    }

    // 블로그 목록 조회
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model, @RequestParam(required = false, defaultValue = "1", value = "page") Long pageNum){
        Page<BlogResponseDTO> pageInfo = blogService.findAll(pageNum);

        final int PAGE_BTN_NUM = 10; // 한 페이지에 보여야 하는 페이징 버튼 그룹의 개수
        int currentPageNum = pageInfo.getNumber() + 1; // 현재 조회중인 페이지(0부터 셈). 강조 스타일 위해 필요
        int endPageNum = (int)Math.ceil(currentPageNum / (double)PAGE_BTN_NUM) * PAGE_BTN_NUM;  // 현재 조회중인 페이지 그룹의 끝번호
        int startPageNum = endPageNum - PAGE_BTN_NUM + 1; // 현재 조회중인 페이지 그룹의 시작번호

        // 마지막 그룹 번호 보정
        endPageNum = Math.min(endPageNum, pageInfo.getTotalPages());

        model.addAttribute("currentPageNum", currentPageNum);
        model.addAttribute("endPageNum", endPageNum);
        model.addAttribute("startPageNum", startPageNum);
        model.addAttribute("pageInfo", pageInfo);
        return "blog/blogList"; // /WEB-INF/views/board/blogList.jsp
    }

    // 블로그 디테일 페이지
    @RequestMapping(value = "/detail/{blogId}", method = RequestMethod.GET)
    public String detail(@PathVariable long blogId, Model model){
        BlogResponseDTO blogFindByIdDTO = blogService.findById(blogId);
        model.addAttribute("blog", blogFindByIdDTO); // 데이터 전달하여 뷰에 뿌려주기
        return "blog/blogDetail"; // /WEB-INF/views/blog/blogDetail.jsp
    }

    // 블로그 생성 맟 수정 폼
    @RequestMapping(value="/new", method = RequestMethod.GET)
    public String updateForm(@RequestParam(required = false, value = "id") Long blogId, Model model) {
        if (blogId == null) {
            model.addAttribute("blog", null);
        } else {
            BlogResponseDTO blog = blogService.findById(blogId);
            model.addAttribute("blog", blog);
        }
        return "blog/newBlog"; // /WEB-INF/views/blog/newBlog.jsp
    }


    // 블로그 삭제 : POST /blog/delete/{blogId}
    @RequestMapping(value = "/delete/{blogId}", method = RequestMethod.POST)
    public String delete(@PathVariable long blogId){
        blogService.deleteById(blogId);
        return "blog/list";
    }

    // 블로그 생성 : POST /blog/create

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String insert(@Valid @RequestBody BlogCreateRequestDTO blogCreateRequestDTO, BindingResult bindingResult){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername(); // email
        blogCreateRequestDTO.setBlogWriter(username);

        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            return "blog/list";
        }

        blogService.save(blogCreateRequestDTO);
        return "redirect:/blog/list";
    }

    // 블로그 수정

    @RequestMapping(value = "/update/{blogId}", method = RequestMethod.POST)
    public String update(@PathVariable long blogId, @Valid @RequestBody BlogUpdateRequestDTO blogUpdateRequestDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            System.out.println(bindingResult.getAllErrors());
            return "redirect:/blog/detail/" + blogId;
        }

        blogService.update(blogId, blogUpdateRequestDTO);
        return "redirect:/blog/detail/" + blogId;
    }

}
