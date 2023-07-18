package com.example.blog.controller;

import com.example.blog.dto.BlogResponseDTO;
import com.example.blog.dto.BlogCreateRequestDTO;
import com.example.blog.dto.BlogUpdateRequestDTO;
import com.example.blog.service.BlogService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/blog")
@Log4j2
public class BlogViewController {
    // 컨트롤러 레이어는 서비스 레이어를 직접 호출해서 사용합니다. -> 의존성 주입
    BlogService blogService;

    @Autowired // 생성자 주입
    public BlogViewController(BlogService blogService){
        this.blogService = blogService;
    }

    // 1. 블로그 목록 조회 : GET /blog/list
    @RequestMapping(value = "/list/{pageNum}", method = RequestMethod.GET)
    public String list(Model model, @PathVariable(required = false) Long pageNum){
        Page<BlogResponseDTO> pageInfo = blogService.findAll(pageNum);

        final int PAGE_BTN_NUM = 10; // 한 페이지에 보여야 하는 페이징 버튼 그룹의 개수
        int currentPageNum = pageInfo.getNumber() + 1; // 현재 조회중인 페이지(0부터 셈). 강조 스타일 위해 필요
        int endPageNum = (int)Math.ceil(currentPageNum / (double)PAGE_BTN_NUM) * PAGE_BTN_NUM;  // 현재 조회중인 페이지 그룹의 끝번호
        int startPageNum = endPageNum - PAGE_BTN_NUM + 1; // 현재 조회중인 페이지 그룹의 시작번호

        // 마지막 그룹 번호 보정
        endPageNum = endPageNum > pageInfo.getTotalPages() ? pageInfo.getTotalPages() : endPageNum;

        model.addAttribute("currentPageNum", currentPageNum);
        model.addAttribute("endPageNum", endPageNum);
        model.addAttribute("startPageNum", startPageNum);
        model.addAttribute("pageInfo", pageInfo);
        return "blog/list"; // /WEB-INF/views/board/list.jsp
    }

    // 2. 블로그 디테일 페이지 : GET /blog/detail/글번호
    @RequestMapping(value = "/detail/{blogId}", method = RequestMethod.GET)
    public String detail(@PathVariable long blogId, Model model){
        BlogResponseDTO blogFindByIdDTO = blogService.findById(blogId);
        model.addAttribute("blog", blogFindByIdDTO); // 데이터 전달하여 뷰에 뿌려주기
        return "blog/detail"; // /WEB-INF/views/blog/detail.jsp
    }

    // 3. 블로그 삭제 : POST /blog/delete/{blogId}
    @RequestMapping(value = "/delete/{blogId}", method = RequestMethod.POST)
    public String delete(@PathVariable long blogId){
        blogService.deleteById(blogId);
        return "redirect:/blog/list";
    }

    // 4. 블로그 생성 : GET /blog/create , POST /blog/insert
    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public String insert(){
        return "blog/blog-form"; // /WEB-INF/views/blog/blog-form.jsp
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public String insert(@Valid BlogCreateRequestDTO blogCreateRequestDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            /*
            에러 메세지 추가 필요m
             */
            System.out.println(bindingResult.getAllErrors());
            return "blog/blog-form";
        }
        blogService.save(blogCreateRequestDTO);
        return "redirect:/blog/list";
    }

    // 5. 블로그 업데이트 : POST /blog/updateform , POST /blog/update/{blogId}
    @RequestMapping(value="/updateform", method = RequestMethod.POST)
    public String update(long blogId, Model model){
        BlogResponseDTO blog = blogService.findById(blogId);
        // .jsp로 보내기 위해 적재합니다.
        model.addAttribute("blog", blog);
        return "blog/blog-update-form"; // /WEB-INF/views/blog/blog-update-form.jsp
    }

    @RequestMapping(value = "/update/{blogId}", method = RequestMethod.POST)
    public String update(@PathVariable long blogId, @Valid BlogUpdateRequestDTO blogUpdateRequestDTO, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            /*
            에러 메세지 추가 필요
             */
            return "redirect:/blog/detail/" + blogId;
        }
        blogService.update(blogId, blogUpdateRequestDTO);
        return "redirect:/blog/detail/" + blogId;
    }

}
