package com.example.blog.entity;

import lombok.*;

import java.time.LocalDateTime;
@Getter @ToString
public class Blog {
    private final long blogId; // declared as AUTO_INCREMENT in MySQL
    private final String blogWriter;
    private String blogTitle;
    private String blogContent;
    private final LocalDateTime publishedAt;
    private LocalDateTime updatedAt;
    private long blogCount;

    @Builder
    public Blog(long blogId, String blogWriter, String blogTitle, String blogContent, LocalDateTime publishedAt, LocalDateTime updatedAt, long blogCount){
        this.blogId = blogId;
        this.blogWriter = blogWriter;
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
        this.updatedAt = updatedAt;
        this.publishedAt = publishedAt;
        this.blogCount = blogCount;
    }

    // Business Logic to change the data
    public void updateBlogTitle(String blogTitle){
        this.blogTitle = blogTitle;
    }
    public void updateBlogContent(String blogContent){
        this.blogContent = blogContent;
    }
    public void updateTime(LocalDateTime updatedAt){ this.updatedAt = updatedAt; }
    public void incrementBlogCount(){ this.blogCount++ ; }
}
