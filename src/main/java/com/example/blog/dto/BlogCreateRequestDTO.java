package com.example.blog.dto;

import com.example.blog.entity.Blog;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogCreateRequestDTO {
    // Save 시에 필요한 멤버변수
    //private long blogId;
    private String writer;
    private String blogTitle;
    private String blogContent;
    //private LocalDateTime publishedAt;
    //private LocalDateTime updatedAt;
    //private long blogCount;

    // Entity -> DTO
    public BlogCreateRequestDTO(Blog blog){
        //this.blogId = blog.getBlogId();
        this.writer = blog.getWriter();
        this.blogTitle = blog.getBlogTitle();
        this.blogContent = blog.getBlogContent();
        //this.publishedAt = blog.getPublishedAt();
        //this.updatedAt = blog.getUpdatedAt();
        //this.blogCount = blog.getBlogCount();
    }
}
