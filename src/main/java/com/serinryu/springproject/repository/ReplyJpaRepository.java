package com.serinryu.springproject.repository;

import com.serinryu.springproject.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {
    List<Reply> findAllByBlogId(long blogId);
    void deleteAllByBlogId(long blogId);

}
