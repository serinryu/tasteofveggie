package com.example.blog.repository;

import com.example.blog.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByBlogId(long blogId);
    void deleteAllByBlogId(long blogId);

}
