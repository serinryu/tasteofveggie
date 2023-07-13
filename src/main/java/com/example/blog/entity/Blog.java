package com.example.blog.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@Getter @ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long blogId; // declared as AUTO_INCREMENT in MySQL

    @Column(nullable = false)
    private String blogWriter;

    @Column(nullable = false)
    private String blogTitle;

    @Column(nullable = false)
    private String blogContent;

    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;
    private Long blogCount;

    @PrePersist // 비영속(new/transient) 상태에서 영속(managed) 상태가 되는 시점 이전에 실행, 즉 save 이전에 실행
    public void setDefaultValue(){
        this.blogCount = this.blogCount == null ? 0 : this.blogCount; // 기본현 변수는 null 일 수 없으므로 blogCount 타입을 Wrapper 클래스(Long)로 수정함
        this.publishedAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate // 영속 상태의 엔티티를 이용하여 데이터 업데이트를 수행하기 이전에 실행, 즉, 두번째 save 시 실행
    public void setUpdateValue(){
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public Blog(long blogId, String blogWriter, String blogTitle, String blogContent, LocalDateTime publishedAt, LocalDateTime updatedAt, long blogCount){
        this.blogId = blogId;
        this.blogWriter = blogWriter;
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
        this.updatedAt = updatedAt;
        this.publishedAt = publishedAt;
        this.blogCount = blogCount;
    }

    // Business Logic to change the data
    public void updateTitleAndContent(String blogTitle, String blogContent){
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
        this.updatedAt = LocalDateTime.now();
    }
    public void incrementBlogCount(){ this.blogCount++ ; }

}
