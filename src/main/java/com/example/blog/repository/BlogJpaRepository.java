package com.example.blog.repository;

import com.example.blog.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogJpaRepository extends JpaRepository<Blog, Long> {
    // void updateBlogCount(Blog blog);
}
