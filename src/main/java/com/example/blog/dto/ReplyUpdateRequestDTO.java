package com.example.blog.dto;

import com.example.blog.entity.Reply;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import static java.time.LocalDateTime.now;

@Getter @ToString
public class ReplyUpdateRequestDTO {

    @NotEmpty
    private String replyContent;

    public ReplyUpdateRequestDTO(){};
    public ReplyUpdateRequestDTO(String replyContent){
        this.replyContent = replyContent;
    }

    // Entity to DTO
    public ReplyUpdateRequestDTO(Reply reply) {
        this.replyContent = reply.getReplyContent();
    }
}
