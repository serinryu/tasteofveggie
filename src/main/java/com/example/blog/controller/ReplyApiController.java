package com.example.blog.controller;

import com.example.blog.dto.ReplyCreateRequestDTO;
import com.example.blog.dto.ReplyResponseDTO;
import com.example.blog.dto.ReplyUpdateRequestDTO;
import com.example.blog.service.ReplyService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
public class ReplyApiController {
    ReplyService replyService;

    private static final Logger logger = LogManager.getLogger(ReplyApiController.class);

    @Autowired
    public ReplyApiController(ReplyService replyService){
        this.replyService = replyService;
    }

    @GetMapping( "/{blogId}/all")
    public ResponseEntity<List<ReplyResponseDTO>> findAllReplies(@PathVariable long blogId){
        List<ReplyResponseDTO> replies = replyService.findAllByBlogId(blogId);
        return ResponseEntity.ok().body(replies);
    }

    @GetMapping( "/{replyId}")
    public ResponseEntity<?> findByReplyId(@PathVariable long replyId){
        ReplyResponseDTO replyFindByIdDTO = replyService.findByReplyId(replyId);
        return ResponseEntity.ok().body(replyFindByIdDTO);
    }

    @PostMapping
    public ResponseEntity<String> addReply(@RequestBody @Valid ReplyCreateRequestDTO replyCreateRequestDTO, BindingResult bindingResult) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the username from the authentication object
        replyCreateRequestDTO.setReplyWriter(username);

        if (bindingResult.hasErrors()) {
            logger.error("Validation errors: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }

        replyService.save(replyCreateRequestDTO);
        logger.info("Reply updated successfully.");
        return ResponseEntity.ok().body("Reply added successfully.");
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<String> deleteReply(@PathVariable long replyId){
        replyService.deleteByReplyId(replyId);
        return ResponseEntity.ok().body("Reply deleted successfully.");
    }

    @PutMapping("/{replyId}")
    public ResponseEntity<String> updateReply(@PathVariable long replyId, @RequestBody @Valid ReplyUpdateRequestDTO replyUpdateRequestDTO){
        replyService.update(replyId, replyUpdateRequestDTO);
        return ResponseEntity.ok().body("Reply updated successfully.");
    }

}