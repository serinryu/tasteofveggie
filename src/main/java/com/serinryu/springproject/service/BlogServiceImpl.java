package com.serinryu.springproject.service;

import com.serinryu.springproject.dto.BlogCreateRequestDTO;
import com.serinryu.springproject.dto.BlogResponseDTO;
import com.serinryu.springproject.dto.BlogUpdateRequestDTO;
import com.serinryu.springproject.entity.Blog;
import com.serinryu.springproject.exception.InvalidDataException;
import com.serinryu.springproject.exception.InternalServerErrorException;
import com.serinryu.springproject.exception.UnauthorizedException;
import com.serinryu.springproject.exception.NotFoundBlogIdException;
import com.serinryu.springproject.repository.BlogJpaRepository;
import com.serinryu.springproject.repository.BlogRepository;
import com.serinryu.springproject.repository.ReplyJpaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
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
        try {
            final int pageSize = 10;
            pageNum = (pageNum <= 0L) ? 1L : pageNum; // 사용자가 0이나 음수를 넣을 경우, 1로 리턴

            Pageable pageable = PageRequest.of(pageNum.intValue() - 1, pageSize);
            Page<Blog> blogPage = blogJpaRepository.findAll(pageable); // JpaRepository 에서 Page<Blog> findAll(Pageable pageable) 자동생성

            if(pageNum > Math.ceil(blogPage.getTotalElements() / (double) pageSize)){
                throw new InvalidDataException("Invalid Page Number");
                // 이후 프론트에서 redirection 처리
            }

            return blogPage.map(BlogResponseDTO::fromEntity);

        } catch (Exception e){
            throw new InternalServerErrorException("Error occurred while retrieving the blog.");
        }
    };

    @Override
    @Transactional // updateBlogCount() 를 Dirty-Checking 으로 수행하므로 (readOnly=true) 걸어놓으면 안됨
    public BlogResponseDTO findById(long blogId) {
        Blog blog = blogJpaRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundBlogIdException("Not Found blogId : " + blogId));

        blog.updateBlogCount();
        // blogJpaRepository.updateBlogCount(blogId); // Dirty-Checking

        return BlogResponseDTO.fromEntity(blog);
    }

    @Override
    @Transactional
    public void save(BlogCreateRequestDTO blogCreateRequestDTO) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        blogCreateRequestDTO.updateBlogWriter(userName);
        Blog blog = blogCreateRequestDTO.toEntity(); // DTO to Entity

        /*
        try {
            blogJpaRepository.save(blog);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error occurred while saving the blog.");
        }
         */

        Blog savedBlog = blogJpaRepository.save(blog);
        if(savedBlog == null){
            throw new InternalServerErrorException("Error occurred while saving the blog.");
        }

    }

    @Override
    @Transactional
    public void deleteById(long blogId) {
        // blogId == null 이거나 해당되는 블로그가 없을 때 예외
        Blog blog = blogJpaRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundBlogIdException("Not Found blogId : " + blogId));

        // 게시글을 작성한 유저인지 확인
        authorizeBlogWriter(blog);

        try {
            // MyBatis 에서 한 메소드당 쿼리문 1개 사용이 보편적이므로 이 두 로직을 합치는 것은 Repository 가 아니라 Service 단에서 진행했음.
            replyJpaRepository.deleteAllByBlogId(blogId);
            blogJpaRepository.deleteById(blogId);
        } catch (Exception e) {
            throw new InternalServerErrorException("Error occurred while deleting the blog.");
        }

    }

    @Override
    @Transactional
    public void update(long blogId, BlogUpdateRequestDTO blogUpdateRequestDTO) {
        Blog blog = blogJpaRepository.findById(blogId)
                .orElseThrow(() -> new NotFoundBlogIdException("Not Found blogId : " + blogId));

        // 게시글을 작성한 유저인지 확인
        authorizeBlogWriter(blog);

        try {
            blog.updateTitleAndContent(blogUpdateRequestDTO.getBlogTitle(), blogUpdateRequestDTO.getBlogContent());
            // // blogJpaRepository.save(blog); // Dirty-Checking
        } catch (InvalidDataException e) {
            throw new InvalidDataException("Invalid data format. Please check your input data.");
        } catch (Exception e) {
            throw new InternalServerErrorException("Error occurred while updating the blog.");
        }

    }

    // 게시글을 작성한 유저인지 확인 (삭제, 수정 시 사용)
    private static void authorizeBlogWriter(Blog blog) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();
        if (!blog.getBlogWriter().equals(userName)) {
            throw new UnauthorizedException("not authorized");
        }
    }

}
