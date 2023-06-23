package com.example.blog.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString @Builder
@AllArgsConstructor
@NoArgsConstructor
public class Reply {
    private long replyId;
    private long blogId;
    private String replyWriter;
    private String replyContent;
    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;

    // Business Logic to change the data
    public void updateReplyContent(String replyContent){ this.replyContent = replyContent; }
    public void updateTime(LocalDateTime updatedAt){ this.updatedAt = updatedAt; }
}
