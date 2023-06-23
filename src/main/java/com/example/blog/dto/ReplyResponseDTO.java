package com.example.blog.dto;

import com.example.blog.entity.Reply;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyResponseDTO {
    private long replyId;
    // blogId 는 사용하지 않음, blogId는 이미 파라미터로 받아서 찾음.
    private String replyWriter;
    private String replyContent;
    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;

    // Entity -> DTO
    // DTO 는 Entity 객체를 이용해서 생성될 수 있어야 한다.
    // DTO 가 Entity 의 하위 개념이기 때문.
    // 그러나, 반대는 성립하지 않는다. (Entity 는 DTO 내부 구조를 알 필요가 없다.)

    // entity 의 데이터로 새로운 dto 를 만들어주는 "생성자"
    public ReplyResponseDTO(Reply reply) {
        this.replyId = reply.getReplyId();
        this.replyWriter = reply.getReplyWriter();
        this.replyContent = reply.getReplyContent();
        this.publishedAt = reply.getPublishedAt();
        this.updatedAt = reply.getUpdatedAt();
    } // 생성자

    /*
    // dto 클래스의 builder 패턴 사용할 수 있음. (선호 차이)
    // 방법의 차이일뿐, 핵심은 EntitytoDto 가 이뤄진다는 것이 핵심이다.
    public ReplyFindByBlogIdDTO entityTodto(Reply reply){
        ReplyFindByBlogIdDTO replyFindByBlogIdDTO = ReplyFindByBlogIdDTO.builder()
                .replyId(reply.getReplyId())
                .replyWriter(reply.getReplyWriter())
                .replyContent(reply.getReplyContent())
                .publishedAt(reply.getPublishedAt())
                .updatedAt(reply.getUpdatedAt())
                .build();
        return replyFindByBlogIdDTO;
    } // dto 리턴
    */


}

