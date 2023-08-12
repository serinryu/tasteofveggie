package com.serinryu.springproject.repository;

import com.serinryu.springproject.entity.Blog;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

// 현재 이 Repository 는 인터페이스일뿐, 구현체는 MyBatis 가 관리한다.
@Mapper // MyBatis 의 관리 받음 + Repository 로 빈 등록됨
public interface BlogRepository {

    // SELECT 구문
    // DTO -> Entity -> DB -> Entity -> DTO
    List<Blog> findAll();
    Blog findById(long blogId);

    // DELETE, INSERT, UPDATE 구문 => DB 수정만 하고 리턴 안함
    // DTO -> Entity -> DB (No need to return)
    void deleteById(long blogId);
    void save(Blog blog);
    void update(Blog blog); // JPA에서는 .save()를 동일하게 쓰지만, 현재 코드에서 메서드 오버로딩도 불가능하고 분리할 방법이 없으므로 메서드명을 다르게 사용합니다.
    void updateBlogCount(Blog blog);
}
