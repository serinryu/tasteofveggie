package com.example.blog.repository;

import com.example.blog.entity.Reply;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class ReplyRepositotyTest {
    @Autowired
    ReplyRepository replyRepository;
    @Autowired
    BlogRepository blogRepository;

    @Test
    @Transactional
    public void findAllByBlogIdTest(){
        // given
        long blogId = 1;
        // when
        List<Reply> result = replyRepository.findAllByBlogId(blogId);
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
        Reply result = replyRepository.findByReplyId(replyId);
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
        replyRepository.deleteByReplyId(replyId);
        // then
        assertEquals(0, replyRepository.findAllByBlogId(2).size());
        assertNull(replyRepository.findByReplyId(replyId));
    }

    @Test
    @Transactional
    public void deleteAllByBlogIdTest(){
        // given
        long blogId = 1;
        // when
        replyRepository.deleteAllByBlodId(blogId);
        // then
        assertEquals(0, replyRepository.findAllByBlogId(blogId).size());
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
        replyRepository.save(reply);

        // then
        List<Reply> resultList = replyRepository.findAllByBlogId(blogId);
        Reply result = resultList.get(resultList.size() - 1); // 마지막 인텍스 요소만 가져오기
        assertEquals(2, replyRepository.findAllByBlogId(blogId).size());
        assertEquals(replyWriter, result.getReplyWriter());
        assertEquals(replyContent, result.getReplyContent());
    }



    @Test
    @Transactional
    public void updateTest(){
        // given
        long replyId = 1;
        String replyContent = "I changed this!";

        Reply reply = replyRepository.findByReplyId(replyId);
        reply.update(replyContent);

        // when
        replyRepository.update(reply);

        // then
        assertEquals(replyId,reply.getReplyId());
        assertEquals(replyContent, reply.getReplyContent());
        assertTrue(reply.getUpdatedAt()
                .isAfter(reply.getPublishedAt())); // updatedAt이 publishedAt보다 이후 시점(after)
    }

}
