package com.example.blog.dto;

import com.example.blog.entity.Reply;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @ToString @NoArgsConstructor
public class ReplyCreateRequestDTO {
    @NotNull
    private long blogId;

    @NotEmpty
    private String replyWriter;

    @NotEmpty
    private String replyContent;

    public ReplyCreateRequestDTO(long blogId, String replyWriter, String replyContent){
        this.blogId = blogId;
        this.replyWriter = replyWriter;
        this.replyContent = replyContent;
    }

    // Entity to DTO
    public ReplyCreateRequestDTO(Reply reply) {
        this.blogId = reply.getBlogId();
        this.replyWriter = reply.getReplyWriter();
        this.replyContent = reply.getReplyContent();
    }
}
