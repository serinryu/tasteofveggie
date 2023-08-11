package com.serinryu.springproject.rest;

import com.serinryu.springproject.dto.ReplyCreateRequestDTO;
import com.serinryu.springproject.dto.ReplyResponseDTO;
import com.serinryu.springproject.dto.ReplyUpdateRequestDTO;
import com.serinryu.springproject.service.ReplyService;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReplyApiController {
    ReplyService replyService;

    private static final Logger logger = LogManager.getLogger(ReplyApiController.class);

    @Autowired
    public ReplyApiController(ReplyService replyService){
        this.replyService = replyService;
    }

    /**
     * Retrieve all replies for a blog
     */
    @GetMapping( "/api/blogs/{blogId}/replies")
    public ResponseEntity<List<ReplyResponseDTO>> findAllReplies(@PathVariable long blogId){

        List<ReplyResponseDTO> replies = replyService.findAllByBlogId(blogId);

        return ResponseEntity.ok().body(replies);
    }

    /**
     * Retrieve a reply by ID
     */
    @GetMapping( "/api/replies/{replyId}")
    public ResponseEntity<ReplyResponseDTO> findByReplyId(@PathVariable long replyId){
        ReplyResponseDTO reply = replyService.findByReplyId(replyId);

        return ResponseEntity.ok().body(reply);
    }

    /**
     * Create a reply
     */
    @PostMapping("/api/replies")
    public ResponseEntity<String> addReply(@RequestBody @Valid ReplyCreateRequestDTO replyCreateRequestDTO, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            logger.error("Validation errors: {}", bindingResult.getAllErrors());
            return ResponseEntity.badRequest().build();
        }

        replyService.save(replyCreateRequestDTO);
        logger.info("Reply updated successfully.");
        return ResponseEntity.ok().body("Success");
    }

    /**
     * Delete a reply
     */
    @DeleteMapping("/api/replies/{replyId}")
    public ResponseEntity<String> deleteReply(@PathVariable long replyId){

        replyService.deleteByReplyId(replyId);
        return ResponseEntity.ok().body("Success");
    }

    /**
     * Update an existing reply
     */
    @PutMapping("/api/replies/{replyId}")
    public ResponseEntity<String> updateReply(@PathVariable long replyId, @RequestBody @Valid ReplyUpdateRequestDTO replyUpdateRequestDTO){

        replyService.update(replyId, replyUpdateRequestDTO);
        return ResponseEntity.ok().body("Success");
    }

}
