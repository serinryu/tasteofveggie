package com.serinryu.springproject.controller;

import com.serinryu.springproject.dto.BlogResponseDTO;
import com.serinryu.springproject.service.BlogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
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
    public String getBlogs(){
        return "blog/blogList"; // /WEB-INF/views/board/blogList.jsp
    }

    @GetMapping("/blogs/{blogId}")
    public String getBlog(){
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
