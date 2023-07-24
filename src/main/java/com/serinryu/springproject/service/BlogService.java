package com.serinryu.springproject.service;

import com.serinryu.springproject.dto.BlogResponseDTO;
import com.serinryu.springproject.dto.BlogCreateRequestDTO;
import com.serinryu.springproject.dto.BlogUpdateRequestDTO;
import org.springframework.data.domain.Page;

public interface BlogService {
    //List<BlogResponseDTO> findAll();
    Page<BlogResponseDTO> findAll(Long pageNum);
    BlogResponseDTO findById(long blogId);
    void deleteById(long blogId);
    void save(BlogCreateRequestDTO blogCreateRequestDTO);
    void update(long blogId, BlogUpdateRequestDTO blogUpdateRequestDTO);
}


