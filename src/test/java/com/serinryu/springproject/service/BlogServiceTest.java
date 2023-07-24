package com.serinryu.springproject.service;

import com.serinryu.springproject.dto.BlogCreateRequestDTO;
import com.serinryu.springproject.dto.BlogResponseDTO;
import com.serinryu.springproject.dto.BlogUpdateRequestDTO;
import com.serinryu.springproject.entity.Blog;
import com.serinryu.springproject.exception.NotFoundBlogIdException;
import com.serinryu.springproject.repository.BlogJpaRepository;
import com.serinryu.springproject.repository.ReplyJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

/**
 * DB 처리 관련한 Repository의 동작은 Repository 테스트에서 이미 확인했으므로,
 * 서비스 레이어에서는 Repository 메서드가 제대로 호출되는지만 확인하면 되므로
 * 실제 Repository 대신 Mock Repository를 사용하면 된다. 따라서 Mockito 사용
 */

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {
    @Mock
    private BlogJpaRepository blogJpaRepository;
    @Mock
    private ReplyJpaRepository replyJpaRepository;
    @InjectMocks
    private BlogServiceImpl blogService;

    @Test
    @Transactional
    public void findAllTest(){
        // given
        // 실제 DB 를 가져오는 지의 테스트는 Repository Test 가 해야하는 영역
        // 여기에서는 Mock 객체를 사용해서 findAll() 하면 Mock 객체를 다 가져오는지 테스트
        List<Blog> blogs = new ArrayList<>();
        blogs.add(new Blog(1L, "Writer 1", "Title 1", "Content 1", LocalDateTime.now(), LocalDateTime.now(), 0));
        blogs.add(new Blog(2L, "Writer 2", "Title 2", "Content 2", LocalDateTime.now(), LocalDateTime.now(), 0));
        blogs.add(new Blog(3L, "Writer 3", "Title 3", "Content 3", LocalDateTime.now(), LocalDateTime.now(), 0));

        Pageable pageable = PageRequest.of(0, 10);
        Page<Blog> blogPage = new PageImpl<>(blogs, pageable, blogs.size());

        Mockito.when(blogJpaRepository.findAll(pageable)).thenReturn(blogPage);

        // when
        Page<BlogResponseDTO> result = blogService.findAll(1L);

        // then
        assertNotNull(result);
        assertEquals(blogs.size(), result.getTotalElements());

        Mockito.verify(blogJpaRepository).findAll(pageable); // blogRepository.findAll 메소드 호출되었는지 확인
    }

    @Test
    @Transactional
    public void findByIdTest_FoundBlog(){
        // Mock 객체를 사용하여 findById 로 조회시 조회한 id 에 해당되는 Mock 객체를 잘 가져오는지 테스트 (assertEquals)
        // given
        long blogId = 1;
        Blog blog = new Blog(blogId, "Writer 1", "Title 1", "Content 1", LocalDateTime.now(), LocalDateTime.now(), 0);
        Mockito.when(blogJpaRepository.findById(blogId)).thenReturn(Optional.of(blog));
        // when
        BlogResponseDTO result = blogService.findById(blogId);
        // then
        assertEquals("Writer 1", result.getBlogWriter());
        assertEquals("Title 1", result.getBlogTitle());
        Mockito.verify(blogJpaRepository).findById(blogId); // blogRepository.findById 메소드 호출되었는지 확인
    }

    @Test
    @Transactional
    public void findByIdTest_NotFoundBlog(){
        // given
        long blogId = 123;
        Mockito.when(blogJpaRepository.findById(blogId)).thenReturn(Optional.empty());
        // when
        // then
        assertThrows(NotFoundBlogIdException.class,
                () -> blogService.findById(blogId));
    }

    @Test
    @Transactional
    public void deleteByIdTest_FoundBlog(){
        // deleteById 시 reply 전체 삭제 메소드와 blog삭제 메소드 모두 호출되는지만 테스트
        // void Method 이므로 assertEquals 불가 -> verify 이용
        // given
        long blogId = 2;
        Blog blog = new Blog(blogId, "Writer 1", "Title 1", "Content 1", LocalDateTime.now(), LocalDateTime.now(), 0);
        Mockito.doNothing().when(blogJpaRepository).deleteById(blogId);
        // Mockito.doNothing().when(blogRepository).deleteById(blogId);
        // when
        blogService.deleteById(blogId);
        // then
        Mockito.verify(replyJpaRepository).deleteAllByBlogId(blogId);
        Mockito.verify(blogJpaRepository).deleteById(blogId);
        assertDoesNotThrow(() -> blogService.deleteById(blogId));
    }

    @Test
    @Transactional
    public void deleteByIdTest_NotFoundBlog(){
        // given
        long blogId = 123;
        Mockito.doThrow(NotFoundBlogIdException.class).when(blogJpaRepository).deleteById(blogId);
        // when
        // then
        assertThrows(NotFoundBlogIdException.class,
                () -> blogService.deleteById(blogId));
    }

    @Test
    @Transactional
    public void saveTest(){
        // given
        BlogCreateRequestDTO blogCreateRequestDTO = new BlogCreateRequestDTO( "Writer 1", "Title 1", "Content 1");
        Mockito.when(blogJpaRepository.save(any(Blog.class))).thenReturn(any(Blog.class));

        // when
        blogService.save(blogCreateRequestDTO); //it internally calls blogRepository.save()

        // then
        Mockito.verify(blogJpaRepository).save(argThat(blog -> blog.getBlogTitle().equals("Title 1")));
    }

    @Test
    @Transactional
    public void updateTest(){
        // update 시 Repository의 update 가 불러와졌는지 테스트 -> verify 이용
        // given
        // 테스트하고 있는 타켓인 update() 함수 로직 보면, update 하기 전에 데이터가 있어야 하므로 findbyId 가 선행되어야 함.
        long blogId = 1;
        Blog existingBlog = new Blog(
                blogId, "writer", "1번제목", "1번내용", LocalDateTime.now(), LocalDateTime.now(), 0
        );
        Mockito.when(blogJpaRepository.findById(blogId)).thenReturn(Optional.of(existingBlog));

        // when
        blogService.update(blogId, new BlogUpdateRequestDTO("00번 제목","00번 내용"));
        BlogResponseDTO blogResponseDTO = blogService.findById(blogId);

        // then
        assertEquals("00번 제목", blogResponseDTO.getBlogTitle());
        assertEquals("00번 내용", blogResponseDTO.getBlogContent());
        // Mockito.verify(blogJpaRepository).save(argThat(blog -> blog.getBlogTitle().equals("00번 제목")));
    }


}
