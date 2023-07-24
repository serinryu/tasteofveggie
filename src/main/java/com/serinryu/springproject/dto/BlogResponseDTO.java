package com.serinryu.springproject.dto;

import com.serinryu.springproject.entity.Blog;
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

    // constructor
    // 생성자의 접근 제한자를 private 으로 설정하여 객체 생성을 정적 팩토리 메서드로만 가능하게 제한함.
    private BlogResponseDTO(Blog blog){
        this.blogId = blog.getBlogId();
        this.blogWriter = blog.getBlogWriter();
        this.blogTitle = blog.getBlogTitle();
        this.blogContent = blog.getBlogContent();
        this.publishedAt = blog.getPublishedAt();
        this.updatedAt = blog.getUpdatedAt();
        this.blogCount = blog.getBlogCount();
    }

    // Entity to DTO
    // Static Method Factory
    public static BlogResponseDTO fromEntity(Blog blog) {
        return new BlogResponseDTO(blog);
    }

}
