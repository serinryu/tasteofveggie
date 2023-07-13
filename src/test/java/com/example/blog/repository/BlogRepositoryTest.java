package com.example.blog.repository;
import com.example.blog.entity.Blog;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
public class BlogRepositoryTest {
    //@Autowired
    //BlogRepository blogRepository; // field-injected DI (테스트코드니까 필드주입 OK)

    @Autowired
    BlogJpaRepository blogJpaRepository;

    @Autowired
    ReplyRepository replyRepository;

    @Test
    @Transactional
    public void findAllTest(){
        // given
        int blogId = 1;
        // when
        List<Blog> blogList = blogJpaRepository.findAll();
        // then
        assertEquals(3, blogList.size());
        assertEquals(2, blogList.get(blogId).getBlogId()); // 자바 List 자료구조 상 인텍스는 0번부터이므로, blogList.get(1) 객체의 ID 번호는 2
    }


    @Test
    @Transactional
    public void findByIdTest(){
        // given
        long blogId = 2;
        // when
        Blog blog = blogJpaRepository.findById(blogId).get();
        // then
        assertEquals(blogId, blog.getBlogId());
        assertEquals("2번유저", blog.getBlogWriter());
        assertEquals("2번제목", blog.getBlogTitle());
    }


    @Test
    @Transactional
    public void deleteByIdTest(){
        // given
        long blogId = 2;
        // when
        replyRepository.deleteAllByBlodId(blogId); // 블로그 삭제 이전 reply 삭제 선행되어야함 (ServiceImpl 에서 두 메소드 합쳐질 예정)
        blogJpaRepository.deleteById(blogId);
        // then
        assertEquals(2, blogJpaRepository.findAll().size());
        assertEquals(Optional.empty(), blogJpaRepository.findById(blogId));
    }


    @Test
    @Transactional
    public void saveTest(){
        // given
        String blogWriter = "4번유저";
        String blogTitle = "4번제목";
        String blogContent = "4번내용";

        // 실제 Entity 를 제작해서 실제 DB 에 save 해보기
        Blog blog = Blog.builder()
                .blogId(0) // 임시값. DB 에서 auto increment 될 것임.
                .blogWriter(blogWriter)
                .blogTitle(blogTitle)
                .blogContent(blogContent)
                .publishedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        // when
        blogJpaRepository.save(blog);

        // then
        List<Blog> resultList = blogJpaRepository.findAll();
        System.out.println(resultList);
        Blog result = resultList.get(3);
        assertEquals(4, resultList.size());
        assertEquals(blogWriter, result.getBlogWriter());
        assertEquals(blogTitle, result.getBlogTitle());
        assertEquals(blogContent, result.getBlogContent());
    }


    @Test
    @Transactional
    public void updateTest(){
        // given
        long blogId = 2;
        String blogTitle = "0번제목";
        String blogContent = "0번내용";

        Blog blog = blogJpaRepository.findById(blogId).get(); // 원본 데이터를 얻어온 다음 내용 update
        blog.updateTitleAndContent(blogTitle, blogContent);

        // when
        blogJpaRepository.save(blog);

        // then
        assertEquals(3, blogJpaRepository.findAll().size());
        assertEquals(blogTitle, blogJpaRepository.findById(blogId).get().getBlogTitle());
        assertEquals(blogContent, blogJpaRepository.findById(blogId).get().getBlogContent());
    }


}
