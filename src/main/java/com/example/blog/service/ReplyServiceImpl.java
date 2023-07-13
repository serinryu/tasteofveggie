package com.example.blog.service;

import com.example.blog.dto.ReplyCreateRequestDTO;
import com.example.blog.dto.ReplyResponseDTO;
import com.example.blog.dto.ReplyUpdateRequestDTO;
import com.example.blog.entity.Reply;
import com.example.blog.exception.ErrorCode;
import com.example.blog.exception.NotFoundReplyByReplyIdException;
import com.example.blog.repository.ReplyJpaRepository;
import com.example.blog.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReplyServiceImpl implements ReplyService {

    ReplyRepository replyRepository;
    ReplyJpaRepository replyJpaRepository;

    @Autowired
    public ReplyServiceImpl(ReplyRepository replyRepository, ReplyJpaRepository replyJpaRepository){
        this.replyRepository = replyRepository;
        this.replyJpaRepository = replyJpaRepository;
    }

    @Override
    public List<ReplyResponseDTO> findAllByBlogId(long blogId) {

        List<Reply> replyList = replyJpaRepository.findAllByBlogId(blogId);

        // Entity to DTO
        List<ReplyResponseDTO> replyResponseDTO = new ArrayList<>();
        for(Reply reply : replyList){
            replyResponseDTO.add(new ReplyResponseDTO(reply)); // Entity 를 DTO 로 변경한 후 add
        }
        return replyResponseDTO;

    }

    @Override
    public ReplyResponseDTO findByReplyId(long replyId) {
        Reply reply = replyJpaRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundReplyByReplyIdException("Not Found replyId : " + replyId));
        return new ReplyResponseDTO(reply);
    }

    @Override
    public void deleteByReplyId(long replyId) {
        // Exception Handling
        Reply reply = replyJpaRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundReplyByReplyIdException("Not Found replyId : " + replyId));
        replyJpaRepository.deleteById(replyId);
    }

    @Override
    public void save(ReplyCreateRequestDTO replyCreateRequestDTO) {
        // DTO -> Entity
        Reply reply = Reply.builder()
                        .blogId(replyCreateRequestDTO.getBlogId())
                                .replyWriter(replyCreateRequestDTO.getReplyWriter()) // 이후 로그인한 사람의 이름으로 수정 필요
                                        .replyContent(replyCreateRequestDTO.getReplyContent())
                                                .build();
        replyJpaRepository.save(reply);
    }

    @Override
    public void update(long replyId, ReplyUpdateRequestDTO replyUpdateRequestDTO) {
        // DTO -> Entity
        Reply reply = replyJpaRepository.findById(replyId)
                .orElseThrow(()-> new NotFoundReplyByReplyIdException("Not Found Reply"));
        reply.update(replyUpdateRequestDTO.getReplyContent());
        replyJpaRepository.save(reply);
    }
}