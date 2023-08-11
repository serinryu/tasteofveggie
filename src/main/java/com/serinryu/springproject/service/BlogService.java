package com.serinryu.springproject.service;

import com.serinryu.springproject.dto.BlogResponseDTO;
import com.serinryu.springproject.dto.BlogCreateRequestDTO;
import com.serinryu.springproject.dto.BlogUpdateRequestDTO;
import org.springframework.data.domain.Page;

public interface BlogService {
    //List<BlogResponseDTO> findAll();
    Page<BlogResponseDTO> findAll(Long pageNum);
    BlogResponseDTO findById(long blogId);
    Long deleteById(long blogId);
    Long save(BlogCreateRequestDTO blogCreateRequestDTO);
    Long update(long blogId, BlogUpdateRequestDTO blogUpdateRequestDTO);
}


