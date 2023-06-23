package com.example.blog.controller;

import com.example.blog.dto.ReplyCreateRequestDTO;
import com.example.blog.dto.ReplyResponseDTO;
import com.example.blog.exception.NotFoundReplyByReplyIdException;
import com.example.blog.service.ReplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reply")
public class ReplyController {
    ReplyService replyService;

    @Autowired
    public ReplyController(ReplyService replyService){
        this.replyService = replyService;
    }

    // 1. 댓글 모두 보기 : GET /reply/2/all
    // http://localhost:8080/reply/2/all => blogId 가 2인 댓글 모두 출력
    @RequestMapping(value = "/{blogId}/all", method = RequestMethod.GET)
    public ResponseEntity<List<ReplyResponseDTO>> findAllReplies(@PathVariable long blogId){
        List<ReplyResponseDTO> replies = replyService.findAllByBlogId(blogId);
        return ResponseEntity.ok().body(replies);
    }

    // 2. Reply Id 로 특정 Reply 만 보기 : GET /reply/1
    // http://localhost:8080/reply/5
    @RequestMapping(value = "/{replyId}", method = RequestMethod.GET)
    public ResponseEntity<?> findByReplyId(@PathVariable long replyId){
        System.out.println(replyId);
        // 서비스에서 특정 번호 리플을 가져옵니다.
        ReplyResponseDTO replyFindByIdDTO = replyService.findByReplyId(replyId);
        if(replyFindByIdDTO == null) {
            try {
                throw new NotFoundReplyByReplyIdException("없는 리플 번호를 조회했습니다");
            } catch (NotFoundReplyByReplyIdException e) {
                e.printStackTrace();
                return ResponseEntity.badRequest().body("찾는 댓글 번호가 없습니다.");
            }
        }
        //return new ResponseEntity<ReplyFindByIdDTO>(replyFindByIdDTO, HttpStatus.OK);
        return ResponseEntity.ok().body(replyFindByIdDTO);
    }

    // 3. 댓글 작성 등록 : POST /reply
    // post방식으로 /reply 주소로 요청이 들어왔을때 실행
    @RequestMapping(value = "", method = RequestMethod.POST)
    public ResponseEntity<String> insertReply(@RequestBody ReplyCreateRequestDTO replyInsertDTO) {
        replyService.save(replyInsertDTO);
        return ResponseEntity.ok().body("댓글 등록이 잘 되었습니다.");
    }

    // 4. 댓글 삭제 : DELETE /reply/1
    // delete 방식으로 /reply/{댓글번호} 주소로 요청이 들어왔을때 실행
    @RequestMapping(value = {"/{replyId}", "/{replyId}/"}, method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteReply(@PathVariable long replyId){
        replyService.deleteByReplyId(replyId);
        return ResponseEntity.ok().body("삭제 완료 되었습니다");
    }
}
