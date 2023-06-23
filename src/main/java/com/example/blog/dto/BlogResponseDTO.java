package com.example.blog.dto;

import com.example.blog.entity.Blog;
import lombok.*;

import java.time.LocalDateTime;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BlogResponseDTO {
    // SELECT 요청 (FindAll, FindById) 시에 필요한 멤버변수만 가져옴 -> DB에서 데이터 가져온 후, 화면단으로 보여줄 데이터
    private long blogId;
    private String writer;
    private String blogTitle;
    private String blogContent;
    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;
    private long blogCount;

    // Entity -> DTO
    public BlogResponseDTO(Blog blog){
        this.blogId = blog.getBlogId();
        this.writer = blog.getWriter();
        this.blogTitle = blog.getBlogTitle();
        this.blogContent = blog.getBlogContent();
        this.publishedAt = blog.getPublishedAt();
        this.updatedAt = blog.getUpdatedAt();
        this.blogCount = blog.getBlogCount();
    }

}
