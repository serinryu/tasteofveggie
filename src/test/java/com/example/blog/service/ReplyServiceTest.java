package com.example.blog.service;

import com.example.blog.dto.ReplyCreateRequestDTO;
import com.example.blog.dto.ReplyResponseDTO;
import com.example.blog.dto.ReplyUpdateRequestDTO;
import com.example.blog.entity.Reply;
import com.example.blog.repository.ReplyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;

/**
 * DB 처리 관련한 Repository의 동작은 Repository 테스트에서 이미 확인했으므로,
 * 서비스 레이어에서는 Repository 메서드가 제대로 호출되는지만 확인하면 되므로
 * 실제 Repository 대신 Mock Repository를 사용하면 된다. 따라서 Mockito 사용
 */

@ExtendWith(MockitoExtension.class)
public class ReplyServiceTest {
    @Mock
    private ReplyRepository replyRepository;

    @InjectMocks
    private ReplyServiceImpl replyService;

    @Test
    @Transactional
    public void findAllBlogIdTest(){
        // Mock 객체를 사용해서 위 메소드 실행 시 특정 BlogId 에 맞는 Mock 객체를 다 가져오는지 테스트
        // 응답을 제대로 받는지 테스트
        // given
        long blogId = 1;
        List<Reply> replies = new ArrayList<>();
        replies.add(new Reply(1, 1, "a", "a apple is wow", LocalDateTime.now(), LocalDateTime.now()));
        Mockito.when(replyRepository.findAllByBlogId(blogId)).thenReturn(replies);
        // when
        List<ReplyResponseDTO> result = replyService.findAllByBlogId(blogId);
        // then
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getReplyId());
        Mockito.verify(replyRepository).findAllByBlogId(blogId);
    }

    @Test
    @Transactional
    public void findByReplyIdTest(){
        // Mock 객체를 사용하여 findByReplyId 로 조회시 조회한 id 에 해당되는 Mock 객체를 잘 가져오는지 테스트 (assertEquals)
        // 응답을 제대로 받는지 테스트
        // given
        long replyId = 1;
        Reply reply = new Reply(1, 1, "a", "a apple is wow", LocalDateTime.now(), LocalDateTime.now());
        Mockito.when(replyRepository.findByReplyId(replyId)).thenReturn(reply);
        // when
        ReplyResponseDTO result = replyService.findByReplyId(replyId);
        // then
        assertEquals("a", result.getReplyWriter());
        assertEquals("a apple is wow", result.getReplyContent());
        Mockito.verify(replyRepository).findByReplyId(replyId);
    }

    @Test
    @Transactional
    public void deleteByReplyIdTest(){
        // 이 메소드 호출 시 Repository의 deleteByReplyId 가 불러와지는지 테스트 (verify 만 이용)
        // 요청을 제대로 주는지 테스트
        // given
        // 서비스 레이어에서 데이터 넘겨주는지의 테스트이므로 DTO 만 제작
        long replyId = 2;
        Mockito.doNothing().when(replyRepository).deleteByReplyId(replyId);
        // when
        replyService.deleteByReplyId(replyId);
        // then
        Mockito.verify(replyRepository).deleteByReplyId(replyId);
    }

    @Test
    @Transactional
    public void saveTest(){
        // 이 메소드 호출 시 Repository의 save 가 불러와졌는가? (verify 만 이용)
        // 요청을 제대로 주는지 테스트
        // given
        // 서비스 레이어에서 데이터 넘겨주는지의 테스트이므로 DTO 만 제작
        ReplyCreateRequestDTO reply = new ReplyCreateRequestDTO(
                0, "Writer", "Content"
        );
        Mockito.doNothing().when(replyRepository).save(any(Reply.class));

        // when
        replyService.save(reply); //it internally calls replyRepository.save(),

        // then
        Mockito.verify(replyRepository).save(argThat(result -> result.getReplyContent().equals("Content")));
    }

    @Test
    @Transactional
    public void updateTest(){
        // 이 메소드 호출 시 Repository의 update 가 불러와졌는가? (verify 만 이용)
        // 테스트대상인 ReplySerive 코드 체크해보면 findByReplyId 메소드가 먼저 선행됨
        // given
        Reply existingReply = new Reply(0, 1, "writer", "content", null, null);
        Mockito.when(replyRepository.findByReplyId(existingReply.getReplyId())).thenReturn(existingReply);

        // when
        replyService.update(new ReplyUpdateRequestDTO(existingReply.getReplyId(),
                "내용 수정함"));

        // then
        Mockito.verify(replyRepository).update(argThat(reply -> reply.getReplyContent().equals("내용 수정함")));

    }
}
