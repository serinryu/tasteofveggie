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

    // Entity to DTO
    // By making the toDto method static, you can call it directly without having to create an instance of the class where it's defined.
    public static ReplyResponseDTO fromEntity(Reply reply) {
        return new ReplyResponseDTO(
                reply.getReplyId(),
                reply.getReplyWriter(),
                reply.getReplyContent(),
                reply.getPublishedAt(),
                reply.getUpdatedAt()
        );
    }
}

