package com.serinryu.springproject.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long replyId;

    @Column(nullable = false)
    private Long blogId;

    @Column(nullable = false)
    private String replyWriter;

    @Column(nullable = false)
    private String replyContent;

    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;

    @PrePersist // 비영속(new/transient) 상태에서 영속(managed) 상태가 되는 시점 이전에 실행, 즉 save 이전에 실행
    public void setDefaultValue(){
        this.publishedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate // 영속 상태의 엔티티를 이용하여 데이터 업데이트를 수행하기 이전에 실행, 즉, 두번째 save 시 실행
    public void setUpdateValue(){
        this.updatedAt = LocalDateTime.now();
    }

    // constructor
    @Builder
    public Reply(Long replyId, Long blogId, String replyWriter, String replyContent, LocalDateTime publishedAt, LocalDateTime updatedAt){
        this.replyId = replyId;
        this.blogId = blogId;
        this.replyWriter = replyWriter;
        this.replyContent = replyContent;
        this.publishedAt = publishedAt;
        this.updatedAt = updatedAt;
    }

    // Business Logic to change the data
    public void updateContent(String replyContent){
        this.replyContent = replyContent;
    }
}
