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

    // 댓글 작성 시 로그인 유저의 이름으로 업데이트 할 때 사용
    public void updateReplyWriter(String username){
        this.replyWriter = username;
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
