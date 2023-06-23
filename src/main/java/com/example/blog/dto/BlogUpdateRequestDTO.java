package com.example.blog.dto;

import com.example.blog.entity.Blog;
import lombok.*;


// DTO 가 엔터티의 하위 개념이므로, DTO 는 엔터티의 구조를 알아야 작동할 수 있지만,
// 엔터티는 DTO의 구조와 상관 없이 작동해야하므로,
// 엔터티를 DTO 로 바꾸는 것은 가능해야 하지만 그 반대는 성립하지 않음

@Getter @Setter @Builder
@AllArgsConstructor @NoArgsConstructor
public class BlogUpdateRequestDTO {
    // UPDATE 시 필요한 데이터 : 글쓴이, 글제목, 글본문, 글번호

    private long blogId;
    //private String writer;
    private String blogTitle;
    private String blogContent;
    //private LocalDateTime publishedAt;
    //private LocalDateTime updatedAt;
    //private long blogCount;

    // DTO 가 엔터티의 하위 개념이므로, DTO 는 엔터티의 구조를 알아야 작동할 수 있지만,
    // 엔터티는 DTO의 구조와 상관 없이 작동해야하므로,
    // 엔터티를 DTO 로 바꾸는 것은 가능해야 하지만 그 반대는 성립하지 않음

    // Entity -> DTO
    public BlogUpdateRequestDTO(Blog blog){
        this.blogId = blog.getBlogId();
        //this.writer = blog.getWriter();
        this.blogTitle = blog.getBlogTitle();
        this.blogContent = blog.getBlogContent();
        //this.publishedAt = blog.getPublishedAt();
        //this.updatedAt = blog.getUpdatedAt();
        //this.blogCount = blog.getBlogCount();
    }

}
