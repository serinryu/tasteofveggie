package com.example.blog.dto;

import com.example.blog.entity.Blog;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter @ToString
public class BlogCreateRequestDTO {
    @NotEmpty
    private final String blogWriter;

    @NotEmpty
    private final String blogTitle;

    @NotEmpty
    private final String blogContent;

    public BlogCreateRequestDTO(String blogWriter, String blogTitle, String blogContent) {
        this.blogWriter = blogWriter;
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
    }

    // Entity -> DTO
    public BlogCreateRequestDTO(Blog blog){
        this.blogWriter = blog.getBlogWriter();
        this.blogTitle = blog.getBlogTitle();
        this.blogContent = blog.getBlogContent();
    }
}
