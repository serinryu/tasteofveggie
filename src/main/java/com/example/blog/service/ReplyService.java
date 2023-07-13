package com.example.blog.service;

import com.example.blog.dto.ReplyCreateRequestDTO;
import com.example.blog.dto.ReplyResponseDTO;
import com.example.blog.dto.ReplyUpdateRequestDTO;

import java.util.List;

public interface ReplyService {
    List<ReplyResponseDTO> findAllByBlogId(long blogId);
    ReplyResponseDTO findByReplyId(long replyId);
    void deleteByReplyId(long replyId);
    void save(ReplyCreateRequestDTO replyCreateRequestDTO);
    void update(long replyId, ReplyUpdateRequestDTO replyUpdateRequestDTO);


}