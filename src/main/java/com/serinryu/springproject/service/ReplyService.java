package com.serinryu.springproject.service;

import com.serinryu.springproject.dto.ReplyCreateRequestDTO;
import com.serinryu.springproject.dto.ReplyResponseDTO;
import com.serinryu.springproject.dto.ReplyUpdateRequestDTO;

import java.util.List;

public interface ReplyService {
    List<ReplyResponseDTO> findAllByBlogId(long blogId);
    ReplyResponseDTO findByReplyId(long replyId);
    Long deleteByReplyId(long replyId);
    Long save(ReplyCreateRequestDTO replyCreateRequestDTO);
    Long update(long replyId, ReplyUpdateRequestDTO replyUpdateRequestDTO);


}