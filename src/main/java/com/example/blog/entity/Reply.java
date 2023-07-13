package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Reply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long replyId;

    @Column(nullable = false)
    private long blogId;

    @Column(nullable = false)
    private String replyWriter;

    @Column(nullable = false)
    private String replyContent;

    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;

    @Builder
    public Reply(long replyId, long blogId, String replyWriter, String replyContent, LocalDateTime publishedAt, LocalDateTime updatedAt){
        this.replyId = replyId;
        this.blogId = blogId;
        this.replyWriter = replyWriter;
        this.replyContent = replyContent;
        this.publishedAt = publishedAt;
        this.updatedAt = updatedAt;
    }
    // Business Logic to change the data
    public void update(String replyContent){
        this.replyContent = replyContent;
        this.updatedAt = LocalDateTime.now();
    }
}
