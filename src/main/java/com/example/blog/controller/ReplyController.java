package com.example.blog.controller;

import com.example.blog.dto.ReplyResponseDTO;
import com.example.blog.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/reply")
public class ReplyController {
    ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService){
        this.replyService = replyService;
    }

    // http://localhost:8080/reply/2/all => blogId 가 2인 댓글 모두 출력
    @RequestMapping(value = "/{blogId}/all", method = RequestMethod.GET)
    public ResponseEntity<List<ReplyResponseDTO>> findAllReplies(
            @PathVariable long blogId){
        List<ReplyResponseDTO> replies = replyService.findAllByBlogId(blogId);

        return ResponseEntity.ok().body(replies);

    }

}
