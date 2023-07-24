package com.serinryu.springproject.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter @Setter @ToString @NoArgsConstructor
public class BlogUpdateRequestDTO {

    @NotEmpty
    private String blogTitle;

    @NotEmpty
    private String blogContent;

    // constructor
    public BlogUpdateRequestDTO(String blogTitle, String blogContent){
        this.blogTitle = blogTitle;
        this.blogContent = blogContent;
    }

}
