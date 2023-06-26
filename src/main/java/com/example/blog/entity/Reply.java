package com.example.blog.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter @ToString
public class Reply {
    private final long replyId;
    private final long blogId;
    private final String replyWriter;
    private String replyContent;
    private final LocalDateTime publishedAt;
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
    public void updateReplyContent(String replyContent){ this.replyContent = replyContent; }
    public void updateTime(LocalDateTime updatedAt){ this.updatedAt = updatedAt; }
}
