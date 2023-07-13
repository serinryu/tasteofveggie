package com.example.blog.dto;

import com.example.blog.entity.Reply;
import lombok.*;

import java.time.LocalDateTime;

@Getter @ToString
public class ReplyResponseDTO {
    private final long replyId;
    private final String replyWriter;
    private final String replyContent;
    private final LocalDateTime publishedAt;
    private final LocalDateTime updatedAt;

    // constructor
    public ReplyResponseDTO(long replyId, String replyWriter, String replyContent, LocalDateTime publishedAt, LocalDateTime updatedAt){
        this.replyId = replyId;
        this.replyWriter = replyWriter;
        this.replyContent = replyContent;
        this.publishedAt = publishedAt;
        this.updatedAt = updatedAt;
    }

    // Entity to DTO (constructor)
    public ReplyResponseDTO(Reply reply) {
        this.replyId = reply.getReplyId();
        this.replyWriter = reply.getReplyWriter();
        this.replyContent = reply.getReplyContent();
        this.publishedAt = reply.getPublishedAt();
        this.updatedAt = reply.getUpdatedAt();
    }
}

