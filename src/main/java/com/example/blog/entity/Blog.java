package com.example.blog.entity;

import lombok.*;
import org.springframework.cglib.core.Local;

import java.text.DateFormat;
import java.sql.Date;
import java.time.LocalDateTime;

// DB 테이블 구조 정의

// Entity 클래스는 DB 테이블에 대응하는 자바 클래스로, 테이블 구조 정의를 위해 사용하는 경우가 많다. (Entity 의 목적성 자체가 데이터 전달용이 아님)
// 따라서, 실질적인 역직렬화(DB -> 자바객체)나 직렬화를 위한 로직에는 DTO라는 클래스를 만들고 활용할 쿼리문에 맞춰 멤버변수를 정의해서 사용한다.
@Getter @ToString
@AllArgsConstructor @Builder // 빌더패턴 생성자를 쓸 수 있도록 해줌
public class Blog {
    // 역직렬화(DB->자바객체)가 가능하도록 blog 테이블 구조에 맞춰서 멤버변수를 선언
    // 네이밍 컨벤션에 맞춰서 변수명 바꿔 작성 (SQL 은 snake_case 을 사용했으나 저버 컨벤션에 맞춰서 camelCase로 작성. 단, 역직렬화를 했음에도 불구하고 변수명이 바뀌어 값이 자동으로 맵핑되지 않는 문제 발생하므로 해결해야 함.)
    private long blogId; // declared as AUTO_INCREMENT in MySQL
    private String writer;
    private String blogTitle;
    private String blogContent;
    private LocalDateTime publishedAt;
    private LocalDateTime updatedAt;
    private long blogCount;

    // blogId 는 SQL 에서 autoincrement 설정되어 있음
    // 결국엔, blogId 가 auto increment 라서 수동으로 DTO -> Entity 만들때 에로사항이 있음

    // Business Logic to change the data
    public void updateBlogTitle(String blogTitle){
        this.blogTitle = blogTitle;
    }
    public void updateBlogContent(String blogContent){
        this.blogContent = blogContent;
    }
    public void updateTime(LocalDateTime updatedAt){ this.updatedAt = updatedAt; }
}
