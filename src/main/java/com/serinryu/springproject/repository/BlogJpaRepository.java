package com.serinryu.springproject.repository;

import com.serinryu.springproject.entity.Blog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BlogJpaRepository extends JpaRepository<Blog, Long> {

    /* Dirty-Checking 사용하므로 필요 X
    @Modifying(clearAutomatically = true)
    @Query("UPDATE Blog b SET b.blogCount = b.blogCount + 1 WHERE b.blogId = :blogId")
    void updateBlogCount(Long blogId);
     */

    /*
    @Modifying
    @Query("DELETE FROM Blog b WHERE b.blogId = :blogId")
    Long deleteByIdAndReturnId(long blogId);
     */
}
