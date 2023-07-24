package com.serinryu.springproject.service;

import com.serinryu.springproject.dto.ReplyCreateRequestDTO;
import com.serinryu.springproject.dto.ReplyResponseDTO;
import com.serinryu.springproject.dto.ReplyUpdateRequestDTO;
import com.serinryu.springproject.entity.Reply;
import com.serinryu.springproject.exception.NotFoundReplyByReplyIdException;
import com.serinryu.springproject.repository.ReplyJpaRepository;
import com.serinryu.springproject.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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
                .map(ReplyResponseDTO::fromEntity)// Entity to DTO
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public ReplyResponseDTO findByReplyId(long replyId) {
        Reply reply = replyJpaRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundReplyByReplyIdException("Not Found replyId : " + replyId));
        return ReplyResponseDTO.fromEntity(reply); // Entity to DTO
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