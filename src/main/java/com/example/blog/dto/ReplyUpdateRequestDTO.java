package com.example.blog.dto;

import com.example.blog.entity.Reply;
import lombok.*;

import static java.time.LocalDateTime.now;

@AllArgsConstructor
@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
public class ReplyUpdateRequestDTO {
    private long replyId;
    private String replyContent;
    //private LocalDateTime updatedAt;

    // Entity -> DTO
    // DTO 는 Entity 객체를 이용해서 생성될 수 있어야 한다.
    // DTO 가 Entity 의 하위 개념이기 때문
    // 그러나, 반대는 성립하지 않는다. (Entity 는 DTO 내부 구조를 알 필요가 없다.)

    // entity 의 데이터로 새로운 dto 를 만들어주는 "생성자"
    public ReplyUpdateRequestDTO(Reply reply) {
        this.replyId = reply.getReplyId();
        this.replyContent = reply.getReplyContent();
        //this.updatedAt = reply.getUpdatedAt();
    } // 생성자
}
