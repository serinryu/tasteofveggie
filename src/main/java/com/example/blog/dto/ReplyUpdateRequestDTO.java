package com.example.blog.dto;

import com.example.blog.entity.Reply;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static java.time.LocalDateTime.now;

@Getter @ToString
public class ReplyUpdateRequestDTO {
    @NotNull
    private final long replyId;

    @NotEmpty
    private final String replyContent;

    public ReplyUpdateRequestDTO(long replyId, String replyContent){
        this.replyId = replyId;
        this.replyContent = replyContent;
    }

    // Entity to DTO
    public ReplyUpdateRequestDTO(Reply reply) {
        this.replyId = reply.getReplyId();
        this.replyContent = reply.getReplyContent();
    }
}
