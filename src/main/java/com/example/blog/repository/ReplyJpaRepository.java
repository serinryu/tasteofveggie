package com.example.blog.repository;

import com.example.blog.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {
}
