package com.example.blog.dto;

import com.example.blog.entity.Blog;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;


// DTO 가 엔터티의 하위 개념이므로, DTO 는 엔터티의 구조를 알아야 작동할 수 있지만,
// 엔터티는 DTO의 구조와 상관 없이 작동해야하므로,
// 엔터티를 DTO 로 바꾸는 것은 가능해야 하지만 그 반대는 성립하지 않음

@Getter @ToString
public class BlogUpdateRequestDTO {
    @NotNull
    private final long blogId;

    @NotEmpty
    private final String blogTitle;

    @NotEmpty
    private final String blogContent;

    public BlogUpdateRequestDTO(long blogId, String blogTitle, String blogContent){
        this.blogId = blogId;
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
    }

    // Entity to DTO
    public BlogUpdateRequestDTO(Blog blog){
        this.blogId = blog.getBlogId();
        this.blogTitle = blog.getBlogTitle();
        this.blogContent = blog.getBlogContent();
    }
}
