package com.serinryu.springproject.dto;

import com.serinryu.springproject.entity.Reply;
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

    // constructor
    // 생성자의 접근 제한자를 private 으로 설정하여 객체 생성을 정적 팩토리 메서드로만 가능하게 제한함.
    private ReplyResponseDTO(Reply reply) {
        this.replyId = reply.getReplyId();
        this.replyWriter = reply.getReplyWriter();
        this.replyContent = reply.getReplyContent();
        this.publishedAt = reply.getPublishedAt();
        this.updatedAt = reply.getUpdatedAt();
    }

    // Entity to DTO
    // Static Factory Method
    public static ReplyResponseDTO fromEntity(Reply reply) {
        return new ReplyResponseDTO(reply);
    }
}

