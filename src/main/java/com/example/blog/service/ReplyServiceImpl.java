package com.example.blog.service;

import com.example.blog.dto.BlogResponseDTO;
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
import java.util.stream.Collectors;

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
    @Transactional(readOnly = true)
    public List<ReplyResponseDTO> findAllByBlogId(long blogId) {
        List<Reply> replyList = replyJpaRepository.findAllByBlogId(blogId);
        return replyList.stream()
                .map(ReplyResponseDTO::new)
                .collect(Collectors.toList()); // Entity to DTO
    }

    @Override
    @Transactional(readOnly = true)
    public ReplyResponseDTO findByReplyId(long replyId) {
        Reply reply = replyJpaRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundReplyByReplyIdException("Not Found replyId : " + replyId));
        return new ReplyResponseDTO(reply); // Entity to DTO
    }

    @Override
    @Transactional
    public void deleteByReplyId(long replyId) {
        Reply reply = replyJpaRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundReplyByReplyIdException("Not Found replyId : " + replyId));
        replyJpaRepository.deleteById(replyId);
    }

    @Override
    @Transactional
    public void save(ReplyCreateRequestDTO replyCreateRequestDTO) {
        Reply reply = replyCreateRequestDTO.toEntity(); // DTO to Entity
        replyJpaRepository.save(reply);
    }

    @Override
    @Transactional
    public void update(long replyId, ReplyUpdateRequestDTO replyUpdateRequestDTO) {
        Reply reply = replyJpaRepository.findById(replyId)
                .orElseThrow(()-> new NotFoundReplyByReplyIdException("Not Found Reply"));
        reply.updateContent(replyUpdateRequestDTO.getReplyContent());
        // replyJpaRepository.save(reply); // Dirty-Checking
    }
}