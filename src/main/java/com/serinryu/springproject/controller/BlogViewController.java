package com.serinryu.springproject.controller;

import com.serinryu.springproject.config.PrincipalDetails;
import com.serinryu.springproject.dto.BlogResponseDTO;
import com.serinryu.springproject.repository.UserRepository;
import com.serinryu.springproject.service.BlogService;
import com.serinryu.springproject.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@Log4j2
public class BlogViewController {
    BlogService blogService;

    @Autowired // 생성자 주입
    public BlogViewController(BlogService blogService){
        this.blogService = blogService;
    }

    @GetMapping("/blogs")
    public String getBlogs(Model model,
                           Authentication authentication,
                           @RequestParam(required = false, defaultValue = "1", value = "page") Long pageNum){

        Page<BlogResponseDTO> pageInfo = blogService.findAll(pageNum);

        /*
        if (authentication != null && authentication.isAuthenticated()) {
            PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();

            log.info(authentication);
            log.info(principal);
            log.info(principal instanceof OAuth2User);


            if (principal instanceof OAuth2User) {
                OAuth2User oAuth2User = (OAuth2User) principal;
                String email = (String) oAuth2User.getAttributes().get("email");
                log.info("🌈You are logged in as ... OAuth2 : " + email);
                model.addAttribute("username", email);
            } else {
                //User user = UserService.findByUserName(authentication.getName());
                log.info("🌈You are logged in as: " + principal.getName());
                model.addAttribute("username", authentication.getName());
            }
        }
        */


        final int PAGE_BTN_NUM = 10; // 한 페이지에 보여야 하는 페이징 버튼 그룹의 개수
        int currentPageNum = pageInfo.getNumber() + 1; // 현재 조회중인 페이지(0부터 셈). 강조 스타일 위해 필요
        int endPageNum = (int)Math.ceil(currentPageNum / (double)PAGE_BTN_NUM) * PAGE_BTN_NUM;  // 현재 조회중인 페이지 그룹의 끝번호
        int startPageNum = endPageNum - PAGE_BTN_NUM + 1; // 현재 조회중인 페이지 그룹의 시작번호

        // 마지막 그룹 번호 보정
        endPageNum = Math.min(endPageNum, pageInfo.getTotalPages());

        //model.addAttribute("username", username);

        model.addAttribute("currentPageNum", currentPageNum);
        model.addAttribute("endPageNum", endPageNum);
        model.addAttribute("startPageNum", startPageNum);
        model.addAttribute("pageInfo", pageInfo);
        return "blog/blogList"; // /WEB-INF/views/board/blogList.jsp
    }

    @GetMapping("/blogs/{blogId}")
    public String getBlog(@PathVariable long blogId, Model model){
        BlogResponseDTO blogFindByIdDTO = blogService.findById(blogId);
        model.addAttribute("blog", blogFindByIdDTO); // 데이터 전달하여 뷰에 뿌려주기
        return "blog/blogDetail"; // /WEB-INF/views/blog/blogDetail.jsp
    }

    @GetMapping("/blogs/new")
    public String newBlogForm(@RequestParam(required = false, value = "id") Long blogId, Model model) {

        if (blogId == null) {
            model.addAttribute("blog", null);
        } else {
            BlogResponseDTO blog = blogService.findById(blogId);
            model.addAttribute("blog", blog);
        }
        return "blog/newBlog"; // /WEB-INF/views/blog/newBlog.jsp
    }

}
