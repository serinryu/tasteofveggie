package com.example.blog.repository;

import com.example.blog.entity.Reply;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReplyRepository {
    // Response(응답) => SELECT 구문 (DB 수정 안하고 값 조회만 함)
    // DTO -> Entity -> DB -> Entity -> DTO
    List<Reply> findAllByBlogId(long blogId);
    Reply findByReplyId(long replyId);

    // Request(요청) => DELETE, INSERT, UPDATE 구문 (DB 수정만 하고 리턴 안함)
    // DTO -> Entity -> DB (No need to return)
    void deleteByReplyId(long blogId);
    void deleteAllByBlodId(long blogId);

    void save(Reply reply);
    void update(Reply reply);
}
