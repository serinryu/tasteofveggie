package com.example.blog.dto;

import com.example.blog.entity.Blog;
import lombok.*;

import java.time.LocalDateTime;
@Getter @ToString
public class BlogResponseDTO {
    private final long blogId;
    private final String blogWriter;
    private final String blogTitle;
    private final String blogContent;
    private final LocalDateTime publishedAt;
    private final LocalDateTime updatedAt;
    private final long blogCount;

    // constructor
    public BlogResponseDTO(long blogId, String blogWriter, String blogTitle, String blogContent, LocalDateTime publishedAt, LocalDateTime updatedAt, long blogCount){
        this.blogId = blogId;
        this.blogWriter = blogWriter;
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
        this.publishedAt = publishedAt;
        this.updatedAt = updatedAt;
        this.blogCount = blogCount;
    }

    // Entity to DTO (constructor)
    public BlogResponseDTO(Blog blog){
        this.blogId = blog.getBlogId();
        this.blogWriter = blog.getBlogWriter();
        this.blogTitle = blog.getBlogTitle();
        this.blogContent = blog.getBlogContent();
        this.publishedAt = blog.getPublishedAt();
        this.updatedAt = blog.getUpdatedAt();
        this.blogCount = blog.getBlogCount();
    }

}
