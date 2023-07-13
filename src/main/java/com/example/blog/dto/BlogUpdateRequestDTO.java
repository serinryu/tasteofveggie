package com.example.blog.dto;

import com.example.blog.entity.Blog;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter @Setter @ToString @NoArgsConstructor
public class BlogUpdateRequestDTO {

    @NotEmpty
    private String blogTitle;

    @NotEmpty
    private String blogContent;

    public BlogUpdateRequestDTO(String blogTitle, String blogContent){
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
    }

    // Entity to DTO
    public BlogUpdateRequestDTO(Blog blog){
        this.blogTitle = blog.getBlogTitle();
        this.blogContent = blog.getBlogContent();
    }
}
