package com.example.blog.service;

import com.example.blog.dto.ReplyCreateRequestDTO;
import com.example.blog.dto.ReplyResponseDTO;
import com.example.blog.dto.ReplyUpdateRequestDTO;
import com.example.blog.entity.Reply;
import com.example.blog.exception.ErrorCode;
import com.example.blog.exception.NotFoundReplyByReplyIdException;
import com.example.blog.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReplyServiceImpl implements ReplyService {

    ReplyRepository replyRepository;

    @Autowired
    public ReplyServiceImpl(ReplyRepository replyRepository){
        this.replyRepository = replyRepository;
    }

    @Override
    public List<ReplyResponseDTO> findAllByBlogId(long blogId) {

        List<Reply> replyList = replyRepository.findAllByBlogId(blogId);

        // Entity to DTO
        List<ReplyResponseDTO> replyResponseDTO = new ArrayList<>();
        for(Reply reply : replyList){
            replyResponseDTO.add(new ReplyResponseDTO(reply)); // Entity 를 DTO 로 변경한 후 add
        }
        return replyResponseDTO;

    }

    @Override
    public ReplyResponseDTO findByReplyId(long replyId) {
        Reply reply = replyRepository.findByReplyId(replyId);

        // Exception Handling
        if(reply == null) {
            throw new NotFoundReplyByReplyIdException("없는 리플 번호를 조회했습니다");
        }

        return new ReplyResponseDTO(reply);
    }

    @Override
    public void deleteByReplyId(long replyId) {
        replyRepository.deleteByReplyId(replyId);
    }

    @Override
    public void save(ReplyCreateRequestDTO replyCreateRequestDTO) {
        // DTO -> Entity (DB 수정하는 로직이기 때문에 불가피하게 필요)
        Reply reply = Reply.builder()
                        .blogId(replyCreateRequestDTO.getBlogId())
                                .replyWriter(replyCreateRequestDTO.getReplyWriter())
                                        .replyContent(replyCreateRequestDTO.getReplyContent())
                                                .build();
        replyRepository.save(reply);
    }

    @Override
    public void update(ReplyUpdateRequestDTO replyUpdateRequestDTO) {
        // DTO -> Entity
        Reply reply = replyRepository.findByReplyId(replyUpdateRequestDTO.getReplyId());
        reply.updateReplyContent(replyUpdateRequestDTO.getReplyContent());
        reply.updateTime(LocalDateTime.now());
        replyRepository.update(reply);
    }
}