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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class ReplyApiController {
    ReplyService replyService;

    @Autowired
    public ReplyApiController(ReplyService replyService){
        this.replyService = replyService;
    }

    /**
     * Retrieve all replies for a blog
     */
    @GetMapping( "/api/blogs/{blogId}/replies")
    public ResponseEntity<Map<String, Object>> findAllReplies(@PathVariable long blogId){

        List<ReplyResponseDTO> replies = replyService.findAllByBlogId(blogId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "성공적으로 조회하였습니다.");
        response.put("timestamp", LocalDateTime.now());
        response.put("data", replies);

        return ResponseEntity.ok().body(response);
    }

    /**
     * Retrieve a reply by ID
     */
    @GetMapping( "/api/replies/{replyId}")
    public ResponseEntity<Map<String, Object>> findByReplyId(@PathVariable long replyId){
        ReplyResponseDTO reply = replyService.findByReplyId(replyId);

        Map<String, Object> response = new HashMap<>();
        response.put("message", "댓글 조회 성공");
        response.put("timestamp", LocalDateTime.now());
        response.put("data", reply);

        return ResponseEntity.ok().body(response);
    }

    /**
     * Create a reply
     */
    @PostMapping("/api/replies")
    public ResponseEntity<Map<String, Object>> addReply(@RequestBody @Valid ReplyCreateRequestDTO replyCreateRequestDTO, BindingResult bindingResult) {

        Map<String, Object> response = new HashMap<>();

        if (bindingResult.hasErrors()) {
            response.put("message", "댓글 등록 실패");
            response.put("timestamp", LocalDateTime.now());
            response.put("data", null);
            return ResponseEntity.badRequest().body(response);
        }

        replyService.save(replyCreateRequestDTO);
        // ReplyResponseDTO savedReply = replyService.save(replyCreateRequestDTO);

        response.put("message", "성공적으로 댓글이 등록되었습니다.");
        response.put("timestamp", LocalDateTime.now());
        Map<String, Object> responseData = new HashMap<>();
        //responseData.put("replyId", savedReply.getReplyId());
        response.put("data", responseData);

        return ResponseEntity.ok().body(response);
    }

    /**
     * Delete a reply
     */
    @DeleteMapping("/api/replies/{replyId}")
    public ResponseEntity<Map<String, Object>> deleteReply(@PathVariable long replyId){

        Map<String, Object> response = new HashMap<>();

        replyService.deleteByReplyId(replyId);

        response.put("message", "댓글 삭제 성공");
        response.put("timestamp", LocalDateTime.now());
        response.put("data", null);
        return ResponseEntity.ok().body(response);
    }

    /**
     * Update an existing reply
     */
    @PutMapping("/api/replies/{replyId}")
    public ResponseEntity<Map<String, Object>> updateReply(@PathVariable long replyId, @RequestBody @Valid ReplyUpdateRequestDTO replyUpdateRequestDTO){

        Map<String, Object> response = new HashMap<>();

        replyService.update(replyId, replyUpdateRequestDTO);
        response.put("message", "댓글 수정 성공");
        response.put("timestamp", LocalDateTime.now());
        response.put("data", null);
        return ResponseEntity.ok().body(response);
    }

}
