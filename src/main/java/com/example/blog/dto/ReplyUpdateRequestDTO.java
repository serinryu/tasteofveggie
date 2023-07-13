package com.example.blog.dto;

import com.example.blog.entity.Reply;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import static java.time.LocalDateTime.now;

@Getter @Setter @ToString @NoArgsConstructor
public class ReplyUpdateRequestDTO {

    @NotEmpty
    private String replyContent;

    // constructor
    public ReplyUpdateRequestDTO(String replyContent){
        this.replyContent = replyContent;
    }

}
