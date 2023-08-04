package com.serinryu.springproject.repository;

import com.serinryu.springproject.entity.Reply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReplyJpaRepository extends JpaRepository<Reply, Long> {
    Optional<List<Reply>> findAllByBlogId(long blogId);
    void deleteAllByBlogId(long blogId);

}
