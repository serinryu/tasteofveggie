package com.example.blog.dto;

import com.example.blog.entity.Reply;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor @Getter @Setter
@Builder @ToString @NoArgsConstructor
public class ReplyCreateRequestDTO {
    @NotNull
    private long blogId;

    @NotNull
    private String replyWriter;

    @NotNull
    private String replyContent;

    // Entity -> DTO
    // DTO 는 Entity 객체를 이용해서 생성될 수 있어야 한다.
    // DTO 가 Entity 의 하위 개념이기 때문
    // 그러나, 반대는 성립하지 않는다. (Entity 는 DTO 내부 구조를 알 필요가 없다.)

    // entity 의 데이터로 새로운 dto 를 만들어주는 "생성자"
    public ReplyCreateRequestDTO(Reply reply) {
        this.blogId = reply.getBlogId();
        this.replyWriter = reply.getReplyWriter();
        this.replyContent = reply.getReplyContent();
    } // 생성자
}
