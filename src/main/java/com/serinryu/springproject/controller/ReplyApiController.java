package com.serinryu.springproject.controller;

import com.serinryu.springproject.dto.BlogResponseDTO;
import com.serinryu.springproject.dto.ReplyCreateRequestDTO;
import com.serinryu.springproject.dto.ReplyResponseDTO;
import com.serinryu.springproject.dto.ReplyUpdateRequestDTO;
import com.serinryu.springproject.exception.ForbiddenException;
import com.serinryu.springproject.exception.NotFoundReplyByReplyIdException;
import com.serinryu.springproject.service.ReplyService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reply")
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

        if (replies == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(replies);
    }

    @GetMapping( "/{replyId}")
    public ResponseEntity<ReplyResponseDTO> findByReplyId(@PathVariable long replyId){
        ReplyResponseDTO reply = replyService.findByReplyId(replyId);

        if (reply == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(reply);
    }

    @PostMapping
    public ResponseEntity<String> addReply(@RequestBody @Valid ReplyCreateRequestDTO replyCreateRequestDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            logger.error("Validation errors: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the username from the authentication object
        replyCreateRequestDTO.setReplyWriter(username);

        replyService.save(replyCreateRequestDTO);
        logger.info("Reply updated successfully.");
        return ResponseEntity.ok().body("Success");
    }

    @DeleteMapping("/{replyId}")
    public ResponseEntity<String> deleteReply(@PathVariable long replyId){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the username from the authentication object
        ReplyResponseDTO reply = replyService.findByReplyId(replyId);

        if (reply == null) {
            throw new NotFoundReplyByReplyIdException("Reply Not Found with id :" + replyId);
        }

        if (!reply.getReplyWriter().equals(username)) {
            throw new ForbiddenException("You are not allowed to delete this blog.");
        }

        replyService.deleteByReplyId(replyId);
        return ResponseEntity.ok().body("Success");
    }

    @PutMapping("/{replyId}")
    public ResponseEntity<String> updateReply(@PathVariable long replyId, @RequestBody @Valid ReplyUpdateRequestDTO replyUpdateRequestDTO){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName(); // Get the username from the authentication object
        ReplyResponseDTO reply = replyService.findByReplyId(replyId);

        if (reply == null) {
            throw new NotFoundReplyByReplyIdException("Reply Not Found with id :" + replyId);
        }

        if (!reply.getReplyWriter().equals(username)) {
            throw new ForbiddenException("You are not allowed to delete this blog.");
        }

        replyService.update(replyId, replyUpdateRequestDTO);
        return ResponseEntity.ok().body("Success");
    }

}
