package com.example.blog.repository;

import com.example.blog.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReplyRepositoryTest {
    @Autowired
    ReplyJpaRepository replyJpaRepository;

    @Test
    @Transactional
    public void findAllByBlogIdTest(){
        // given
        long blogId = 1;
        // when
        List<Reply> result = replyJpaRepository.findAllByBlogId(blogId);
        // then
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getReplyId());
    }

    @Test
    @Transactional
    public void findByReplyIdTest(){
        // given
        long replyId = 3;
        // when
        Reply result = replyJpaRepository.findById(replyId).get();
        // then
        assertEquals("c", result.getReplyWriter());
        assertEquals(3, result.getReplyId());
    }

    @Test
    @Transactional
    public void deleteByReplyIdTest(){
        // given
        long replyId = 2;
        // when
        replyJpaRepository.deleteById(replyId);
        // then
        assertEquals(0, replyJpaRepository.findAllByBlogId(2).size());
        assertEquals(Optional.empty(), replyJpaRepository.findById(replyId));
    }

    @Test
    @Transactional
    public void deleteAllByBlogIdTest(){
        // given
        long blogId = 1;
        // when
        replyJpaRepository.deleteAllByBlogId(blogId);
        // then
        assertEquals(0, replyJpaRepository.findAllByBlogId(blogId).size());
    }

    @Test
    @Transactional
    public void saveTest(){
        // given
        long blogId = 1;
        String replyWriter = "Julie";
        String replyContent = "This is my comment";

        // 실제 Entity 제작해서 실제 DB 에 save 해보기
        Reply reply = Reply.builder()
                .replyId(0) // 임시값. DB에서 auto increment 될것임.
                .blogId(blogId)
                .replyWriter(replyWriter)
                .replyContent(replyContent)
                .publishedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // when
        replyJpaRepository.save(reply);

        // then
        List<Reply> resultList = replyJpaRepository.findAllByBlogId(blogId);
        Reply result = resultList.get(resultList.size() - 1); // 마지막 인텍스 요소만 가져오기
        assertEquals(2, replyJpaRepository.findAllByBlogId(blogId).size());
        assertEquals(replyWriter, result.getReplyWriter());
        assertEquals(replyContent, result.getReplyContent());
    }



    @Test
    @Transactional
    public void updateTest(){
        // given
        long replyId = 1;
        String replyContent = "I changed this!";

        Reply reply = replyJpaRepository.findById(replyId).get();
        reply.updateContent(replyContent);

        // when
        replyJpaRepository.save(reply);

        System.out.println(replyJpaRepository.findById(replyId));

        // then
        assertEquals(replyId,replyJpaRepository.findById(replyId).get().getReplyId());
        assertEquals(replyContent, replyJpaRepository.findById(replyId).get().getReplyContent());
        //assertTrue(replyJpaRepository.findById(replyId).get().getUpdatedAt()
        //        .isAfter(replyJpaRepository.findById(replyId).get().getPublishedAt())); // updatedAt이 publishedAt보다 이후 시점(after)
    }

}
