package com.serinryu.springproject.dto;

import com.serinryu.springproject.entity.Blog;
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
    public BlogCreateRequestDTO(String blogTitle, String blogContent) {
        //this.blogWriter = blogWriter;
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
    }

    // 사용자가 로그인 시 로그인하고 있는 사용자의 이름으로 업데이트 할 때 사용
    public void updateBlogWriter(String blogWriter){
        this.blogWriter = blogWriter;
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
