package com.example.blog.repository;

import com.example.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BlogJpaRepository extends JpaRepository<Blog, Long> {

    @Modifying
    @Query("UPDATE Blog b SET b.blogCount = b.blogCount + 1 WHERE b.blogId = :blogId")
    void updateBlogCount(Long blogId);
}
