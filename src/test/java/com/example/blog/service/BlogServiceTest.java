package com.example.blog.service;

import com.example.blog.dto.BlogCreateRequestDTO;
import com.example.blog.dto.BlogResponseDTO;
import com.example.blog.dto.BlogUpdateRequestDTO;
import com.example.blog.entity.Blog;
import com.example.blog.repository.ReplyRepository;
import com.example.blog.repository.BlogRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.argThat;

/**
 * DB 처리 관련한 Repository의 동작은 Repository 테스트에서 이미 확인했으므로,
 * 서비스 레이어에서는 Repository 메서드가 제대로 호출되는지만 확인하면 되므로
 * 실제 Repository 대신 Mock Repository를 사용하면 된다. 따라서 Mockito 사용
 */

@ExtendWith(MockitoExtension.class)
public class BlogServiceTest {
    @Mock
    private BlogRepository blogRepository;
    @Mock
    private  ReplyRepository replyRepository;
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
        Mockito.when(blogRepository.findAll()).thenReturn(blogs);
        // when
        List<BlogResponseDTO> blogList = blogService.findAll();
        // then
        assertEquals(3, blogList.size());
        assertEquals(2, blogList.get(1).getBlogId());
    }

    @Test
    @Transactional
    public void findByIdTest(){
        // Mock 객체를 사용하여 findById 로 조회시 조회한 id 에 해당되는 Mock 객체를 잘 가져오는지 테스트 (assertEquals)
        // given
        Blog blog = new Blog(1L, "Writer 1", "Title 1", "Content 1", LocalDateTime.now(), LocalDateTime.now(), 0);
        Mockito.when(blogRepository.findById(1)).thenReturn(blog);
        // when
        BlogResponseDTO result = blogService.findById(1);
        // then
        assertEquals("Writer 1", result.getWriter());
        assertEquals("Title 1", result.getBlogTitle());
    }

    @Test
    @Transactional
    public void deleteByIdTest(){
        // deleteById 시 reply 전체 삭제 메소드와 blog삭제 메소드 모두 호출되는지만 테스트 (verify 이용)
        // given
        long blogId = 2;
        // Mock the behavior of the blogRepository : ensure that it doesn't perform any action when called with the provided blogId.
        Mockito.doNothing().when(blogRepository).deleteById(blogId);
        // when
        blogService.deleteById(blogId);
        // then
        Mockito.verify(replyRepository).deleteAllByBlodId(blogId);
        Mockito.verify(blogRepository).deleteById(blogId);
    }

    @Test
    @Transactional
    public void saveTest(){
        // save 시 Repository의 save 메소드가 호출되는지만 테스트 (verify 이용)
        // given
        // 서비스 레이어에서의 테스트이므로 DTO 만 제작
        BlogCreateRequestDTO blogCreateRequestDTO = new BlogCreateRequestDTO( "Writer 1", "Title 1", "Content 1");
        Mockito.doNothing().when(blogRepository).save(argThat(blog -> blog.getBlogTitle().equals("Title 1"))); // save(any(Blog.class))

        // when
        blogService.save(blogCreateRequestDTO); //it internally calls blogRepository.save()

        // then
        Mockito.verify(blogRepository).save(argThat(blog -> blog.getBlogTitle().equals("Title 1")));
    }

    @Test
    @Transactional
    public void updateTest(){
        // update 시 Repository의 update 가 불러와졌는지 테스트 (verify 이용)
        // given
        // update 하기 전에 데이터가 있어야 하므로 findbyId 가 선행되어야 함. => 테스트하고 있는 타켓인 update() 함수 로직 보기
        Blog existingBlog = new Blog(
                1, "writer", "1번제목", "1번내용", LocalDateTime.now(), LocalDateTime.now(), 0
        );
        Mockito.when(blogRepository.findById(existingBlog.getBlogId())).thenReturn(existingBlog);

        // when
        blogService.update(new BlogUpdateRequestDTO(existingBlog.getBlogId(),
                "00번 제목",
                "00번 내용"));

        // then
        Mockito.verify(blogRepository).update(argThat(blog -> blog.getBlogTitle().equals("00번 제목")));
    }


}
