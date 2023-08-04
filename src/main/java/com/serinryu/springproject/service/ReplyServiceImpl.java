package com.serinryu.springproject.service;

import com.serinryu.springproject.dto.ReplyCreateRequestDTO;
import com.serinryu.springproject.dto.ReplyResponseDTO;
import com.serinryu.springproject.dto.ReplyUpdateRequestDTO;
import com.serinryu.springproject.entity.Blog;
import com.serinryu.springproject.entity.Reply;
import com.serinryu.springproject.exception.*;
import com.serinryu.springproject.repository.ReplyJpaRepository;
import com.serinryu.springproject.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
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
        List<Reply> replyList = replyJpaRepository.findAllByBlogId(blogId)
                .orElseThrow(() -> new NotFoundBlogIdException("Not Found blogId : " + blogId));
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
    public void save(ReplyCreateRequestDTO replyCreateRequestDTO) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        replyCreateRequestDTO.updateReplyWriter(userName);
        Reply reply = replyCreateRequestDTO.toEntity(); // DTO to Entity

        try {
            replyJpaRepository.save(reply);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error occurred while saving the reply.");
        }
    }

    @Override
    @Transactional
    public void deleteByReplyId(long replyId) {
        Reply reply = replyJpaRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundReplyByReplyIdException("Not Found replyId : " + replyId));

        // 댓글을 작성한 유저인지 확인
        authorizeReplyWriter(reply);

        try {
            replyJpaRepository.deleteById(replyId);
        } catch (Exception e){
            throw new InternalServerErrorException("Error occurred while deleting the reply.");
        }
    }

    @Override
    @Transactional
    public void update(long replyId, ReplyUpdateRequestDTO replyUpdateRequestDTO) {
        Reply reply = replyJpaRepository.findById(replyId)
                .orElseThrow(() -> new NotFoundReplyByReplyIdException("Not Found Reply"));

        // 댓글을 작성한 유저인지 확인
        authorizeReplyWriter(reply);

        try {
            reply.updateContent(replyUpdateRequestDTO.getReplyContent());
            // replyJpaRepository.save(reply); // Dirty-Checking
        } catch (InvalidDataException e) {
            throw new InvalidDataException("Invalid data format. Please check your input data.");
        } catch (Exception e) {
            throw new InternalServerErrorException("Error occurred while updating the blog.");
        }
    }

    // 게시글을 작성한 유저인지 확인 (삭제, 수정 시 사용)
    private static void authorizeReplyWriter(Reply reply) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!reply.getReplyWriter().equals(userName)) {
            throw new UnauthorizedException("not authorized");
        }
    }
}