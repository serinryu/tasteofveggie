package com.example.blog.service;

import com.example.blog.dto.BlogCreateRequestDTO;
import com.example.blog.dto.BlogResponseDTO;
import com.example.blog.dto.BlogUpdateRequestDTO;
import com.example.blog.entity.Blog;
import com.example.blog.exception.NotFoundBlogIdException;
import com.example.blog.repository.BlogJpaRepository;
import com.example.blog.repository.BlogRepository;
import com.example.blog.repository.ReplyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlogServiceImpl implements BlogService {
    // 서비스 레이어는 레포지토리 레이어를 직접 호출하여 사용한다. -> 의존성 주입
    BlogRepository blogRepository; // MyBatis, 현재는 JPA 로 사용하고 있음.
    BlogJpaRepository blogJpaRepository; // JPA
    ReplyJpaRepository replyJpaRepository; // blog 삭제 시 연결되는 reply 전부 삭제 로직 떄문에 호출 필요

    @Autowired // 생성자 주입
    public BlogServiceImpl(BlogRepository blogRepository, BlogJpaRepository blogJpaRepository, ReplyJpaRepository replyJpaRepository){ // 생성자 주입
        this.blogRepository = blogRepository;
        this.blogJpaRepository = blogJpaRepository;
        this.replyJpaRepository = replyJpaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BlogResponseDTO> findAll(Long pageNum){
        Pageable pageable = PageRequest.of(pageNum.intValue()-1, 10);
        return blogJpaRepository.findAll(pageable)
                .map(BlogResponseDTO::fromEntity);
    };

    @Override
    @Transactional(readOnly = true)
    public BlogResponseDTO findById(long blogId) {
        Blog blog = blogJpaRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundBlogIdException("Not Found blogId : " + blogId));
        blog.updateBlogCount();
        // blogJpaRepository.updateBlogCount(blogId); // Dirty-Checking
        return BlogResponseDTO.fromEntity(blog);
    }

    @Override
    @Transactional
    public void deleteById(long blogId) {
        // MyBatis 에서 한 메소드당 쿼리문 1개 사용이 보편적이므로 이 두 로직을 합치는 것은 Repository 가 아니라 Service 단에서 진행했음.
        replyJpaRepository.deleteAllByBlogId(blogId);
        blogJpaRepository.deleteById(blogId);
    }

    @Override
    @Transactional
    public void save(BlogCreateRequestDTO blogCreateRequestDTO) {
        Blog blog = blogCreateRequestDTO.toEntity(); // DTO to Entity
        blogJpaRepository.save(blog);
    }

    @Override
    @Transactional
    public void update(long blogId, BlogUpdateRequestDTO blogUpdateRequestDTO) {
        Blog blog = blogJpaRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundBlogIdException("Not Found blogId : " + blogId));
        blog.updateTitleAndContent(blogUpdateRequestDTO.getBlogTitle(), blogUpdateRequestDTO.getBlogContent());
        // blogJpaRepository.save(blog); // Dirty-Checking
    }

}
