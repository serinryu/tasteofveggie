package com.example.blog.service;

import com.example.blog.dto.BlogResponseDTO;
import com.example.blog.dto.BlogCreateRequestDTO;
import com.example.blog.dto.BlogUpdateRequestDTO;
import org.springframework.data.domain.Page;

public interface BlogService {
    //List<BlogResponseDTO> findAll();
    Page<BlogResponseDTO> findAll(Long pageNum);
    BlogResponseDTO findById(long blogId);
    void deleteById(long blogId);
    void save(BlogCreateRequestDTO blogCreateRequestDTO);
    void update(long blogId, BlogUpdateRequestDTO blogUpdateRequestDTO);
}


