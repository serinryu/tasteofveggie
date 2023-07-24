package com.serinryu.springproject.dto;

import com.serinryu.springproject.entity.Reply;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @ToString @NoArgsConstructor
public class ReplyCreateRequestDTO {
    @NotNull
    private long blogId;

    private String replyWriter;

    @NotEmpty
    private String replyContent;

    // constructor
    public ReplyCreateRequestDTO(long blogId, String replyWriter, String replyContent){
        this.blogId = blogId;
        this.replyWriter = replyWriter;
        this.replyContent = replyContent;
    }

    // DTO to Entity
    public Reply toEntity(){
        return Reply.builder()
                .blogId(this.blogId)
                .replyWriter(this.replyWriter)
                .replyContent(this.replyContent)
                .build();
    }
}
