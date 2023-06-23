package com.example.blog.controller;

import com.example.blog.dto.BlogResponseDTO;
import com.example.blog.dto.BlogCreateRequestDTO;
import com.example.blog.dto.BlogUpdateRequestDTO;
import com.example.blog.exception.NotFoundBlogIdException;
import com.example.blog.service.BlogService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@Controller // 빈 등록 + URL Mapping 처리 기능
@RequestMapping("/blog")
@Log4j2
public class BlogController {
    // 컨트롤러 레이어는 서비스 레이어를 직접 호출해서 사용합니다. -> 의존성 주입
    BlogService blogService;

    @Autowired // 생성자 주입
    public BlogController(BlogService blogService){
        this.blogService = blogService;
    }

    // 1. 블로그 목록 조회 : /blog/list
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(Model model){
        List<BlogResponseDTO> blogList = blogService.findAll(); // Service 객체를 이용해 게시글 전체를 얻어온다.
        model.addAttribute("blogList", blogList); // 얻어온 게시글은 .jsp 로 보낼 수 있도록 적재한다.
        return "board/list"; // /WEB-INF/views/board/list.jsp
    }

    // 2. 블로그 디테일 페이지 : /blog/detail/글번호
    @RequestMapping(value = "/detail/{blogId}", method = RequestMethod.GET)
    public String detail(@PathVariable long blogId, Model model){
        BlogResponseDTO blogFindByIdDTO = blogService.findById(blogId);

        if(blogFindByIdDTO == null){
            // Exception Handling (에러 메세지만 보고 알 수 있도록 custom)
            try {
                throw new NotFoundBlogIdException("Not Found blogId : " + blogId);
            } catch (NotFoundBlogIdException e) {
                e.printStackTrace(); // tracing (예외메세지 체크). 그러나, 현재 같은 경우는 어떤 예외메세지인지 알고 있으므로 해줄 필요는 없음
                return "blog/NotFoundBlogIdExceptionResultPage";
            }
        }

        model.addAttribute("blog", blogFindByIdDTO); // 데이터 전달하여 뷰에 뿌려주기
        // model.addAttribute("blog", blogService.findById(blogId));
        return "blog/detail"; // /WEB-INF/views/blog/detail.jsp
    }

    // 3. 블로그 생성
    @RequestMapping(value = "/insert", method = RequestMethod.GET)
    public String insert(){
        return "blog/blog-form"; // /WEB-INF/views/blog/blog-form.jsp
    }

    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public String insert(BlogCreateRequestDTO blogCreateRequestDTO){
        blogService.save(blogCreateRequestDTO);
        return "redirect:/blog/list";
    }

    // 4. 블로그 삭제
    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(long blogId){
        blogService.deleteById(blogId);
        return "redirect:/blog/list";
    }

    // 5. 블로그 업데이트
    @RequestMapping(value="/updateform", method = RequestMethod.POST)
    public String update(long blogId, Model model){
        BlogResponseDTO blog = blogService.findById(blogId);
        // .jsp로 보내기 위해 적재합니다.
        model.addAttribute("blog", blog);
        return "blog/blog-update-form"; // /WEB-INF/views/blog/blog-update-form.jsp
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public String update(BlogUpdateRequestDTO blogUpdateRequestDTO){
        log.info(blogUpdateRequestDTO);
        blogService.update(blogUpdateRequestDTO);
        return "redirect:/blog/list";
    }

}
