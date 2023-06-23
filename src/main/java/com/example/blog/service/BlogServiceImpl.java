package com.example.blog.service;

import com.example.blog.dto.BlogCreateRequestDTO;
import com.example.blog.dto.BlogResponseDTO;
import com.example.blog.dto.BlogUpdateRequestDTO;
import com.example.blog.entity.Blog;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.ReplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlogServiceImpl implements BlogService {
    // 서비스 레이어는 레포지토리 레이어를 직접 호출하여 사용한다. -> 의존성 주입
    // @Autowired 를 여기에 붙여도 의존성 주입은 된다. 그러나, 필드 주입이므로 지양. 생성자 주입을 하자.
    BlogRepository blogRepository;
    ReplyRepository replyRepository; // blog 삭제 시 연결되는 reply 전부 삭제 로직 떄문에 호출 필요

    @Autowired // 생성자 주입
    public BlogServiceImpl(BlogRepository blogRepository, ReplyRepository replyRepository){ // 생성자 주입
        this.blogRepository = blogRepository;
        this.replyRepository = replyRepository;
    }

    @Override
    public List<BlogResponseDTO> findAll() {
        List<Blog> blogList = blogRepository.findAll(); // Repository 에는 Entity 를 보내줌

        // Entity to DTO
        List<BlogResponseDTO> blogResponseDTOList = new ArrayList<>();
        for(Blog blog : blogList){
            blogResponseDTOList.add(new BlogResponseDTO(blog)); // Entity 를 DTO 로 변경한 후 add
        }
        return blogResponseDTOList;
    }

    @Override
    public BlogResponseDTO findById(long blogId) {
        Blog blog = blogRepository.findById(blogId); // Repository 에서 Entity 를 리턴함
        // Entity to DTO
        return new BlogResponseDTO(blog);
    }

    @Override
    public void deleteById(long blogId) {
        // MyBatis 에서 한 메소드당 쿼리문 1개 사용이 보편적이므로 이 두 로직을 합치는 것은
        // Repository 가 아니라 Service 단에서 진행했음.
        replyRepository.deleteAllByBlodId(blogId);
        blogRepository.deleteById(blogId);
    }

    @Override
    public void save(BlogCreateRequestDTO blogCreateRequestDTO) {
        // DTO to Entity : 받은 DTO 를 Entity 로 변환이 불가피하게 필요. DB 수정하는 로직이기 때문...
        // DTO에서 받은 값을 사용하여 Blog의 빌더 패턴을 사용해 구현
        Blog blog = Blog.builder()
                .writer(blogCreateRequestDTO.getWriter())
                .blogTitle(blogCreateRequestDTO.getBlogTitle())
                .blogContent(blogCreateRequestDTO.getBlogContent())
                .publishedAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        blogRepository.save(blog);
    }

    @Override
    public void update(BlogUpdateRequestDTO blogUpdateRequestDTO) {
        // DTO -> Entity : 받은 DTO 를 Entity 로 변환이 불가피하게 필요. DB 수정하는 로직이기 때문....
        // Blog 의 일부 필드민 수정. Blog Entity에 수정 메소드 구현하여 사용
        Blog blog = blogRepository.findById(blogUpdateRequestDTO.getBlogId());
        blog.updateBlogTitle(blogUpdateRequestDTO.getBlogTitle());
        blog.updateBlogContent(blogUpdateRequestDTO.getBlogContent());
        blog.updateTime(LocalDateTime.now());

        blogRepository.update(blog);
    }

}
