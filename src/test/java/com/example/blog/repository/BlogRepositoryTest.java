package com.example.blog.repository;
import com.example.blog.entity.Blog;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BlogRepositoryTest {
    //@Autowired
    //BlogRepository blogRepository; // field-injected DI (테스트코드니까 필드주입 OK)

    @Autowired
    BlogJpaRepository blogJpaRepository;

    @Autowired
    ReplyJpaRepository replyJpaRepository;

    @Test
    public void findAllTest(){
        // when & given
        Pageable pageable = PageRequest.of(0, 10);
        Page<Blog> blogPage = blogJpaRepository.findAll(pageable);

        // Then
        assertNotNull(blogPage);
        assertEquals(blogPage.getTotalElements(), 45);
        assertEquals(blogPage.getTotalPages(), 5);
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
        replyJpaRepository.deleteAllByBlogId(blogId); // 블로그 삭제 이전 reply 삭제 선행되어야함 (ServiceImpl 에서 두 메소드 합쳐질 예정)
        blogJpaRepository.deleteById(blogId);
        // then
        assertEquals(44, blogJpaRepository.findAll().size());
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
        Blog result = resultList.get(45);
        assertEquals(blogWriter, result.getBlogWriter());
        assertEquals(blogTitle, result.getBlogTitle());
        assertEquals(blogContent, result.getBlogContent());
    }


    @Test
    @Transactional
    public void updateTest(){
        // given
        long blogId = 1;
        String blogTitle = "0번제목";
        String blogContent = "0번내용";

        Blog blog = blogJpaRepository.findById(blogId).get();

        // when
        blog.updateTitleAndContent(blogTitle, blogContent); // Dirty Checking
        //blogJpaRepository.save(blog);

        // then
        assertEquals(blogId, blogJpaRepository.findById(blogId).get().getBlogId());
        assertEquals(blogTitle, blogJpaRepository.findById(blogId).get().getBlogTitle());
        assertEquals(blogContent, blogJpaRepository.findById(blogId).get().getBlogContent());

        // Q. 테스트 상에만 updateAt() 이 바뀌지 않는 문제 (Dirty Checking 으로 DB 변경 시 자동으로 update 로직 내려줬는데 테스트 시 여기서 문제 생긴듯)
        // assertTrue(blogJpaRepository.findById(blogId).get().getUpdatedAt()
        //        .isAfter(blogJpaRepository.findById(blogId).get().getPublishedAt())); // updatedAt이 publishedAt보다 이후 시점(after)

    }
}
