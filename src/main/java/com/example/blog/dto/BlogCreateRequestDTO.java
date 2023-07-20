package com.example.blog.dto;

import com.example.blog.entity.Blog;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString @NoArgsConstructor
public class BlogCreateRequestDTO {

    private String blogWriter;

    @NotEmpty
    private String blogTitle;

    @NotEmpty
    private String blogContent;

    // constructor
    public BlogCreateRequestDTO(String blogWriter, String blogTitle, String blogContent) {
        this.blogWriter = blogWriter;
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
    }

    // (Optional) DTO to Entity : DB 에 request 보내 수정하는 DTO 이므로 Entity 로의 변환이 필수이므로 함수로 제작
    public Blog toEntity(){
        return Blog.builder()
                .blogWriter(this.blogWriter)
                .blogTitle(this.blogTitle)
                .blogContent(this.blogContent)
                .publishedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}
