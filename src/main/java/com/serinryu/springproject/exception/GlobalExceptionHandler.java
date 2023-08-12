package com.serinryu.springproject.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice // 여러 컨트롤러에 대해 전역적으로 ExceptionHandler를 적용
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(InternalServerErrorException e) {
        log.error("InternalServerErrorException", e);
        ErrorResponse response = new ErrorResponse(ErrorCode.SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(500));
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> UnauthorizedHandler(UnauthorizedException e){
        log.error("UnauthorizedException", e);
        ErrorResponse response = new ErrorResponse(ErrorCode.AUTHENTICATION_ERROR);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(401));
    }

    @ExceptionHandler(InvalidMemberRoleException.class)
    public ResponseEntity<ErrorResponse> InvalidMemberRoleHandler(InvalidMemberRoleException e){
        log.error("InvalidMemberRoleException", e);
        ErrorResponse response = new ErrorResponse(ErrorCode.INVALID_MEMBER_ROLE);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(403));
    }

    @ExceptionHandler(NotFoundBlogIdException.class)
    public ResponseEntity<ErrorResponse> NotFoundBlogIdHandler(NotFoundBlogIdException e){
        log.error("NotFoundBlogIdException", e);
        ErrorResponse response = new ErrorResponse(ErrorCode.BLOG_NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(NotFoundReplyByReplyIdException.class)
    public ResponseEntity<ErrorResponse> NotFoundReplyByReplyIdHandler(NotFoundReplyByReplyIdException e){
        log.error("NotFoundReplyByReplyIdException", e);
        ErrorResponse response = new ErrorResponse(ErrorCode.REPLY_NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(400));
    }

    @ExceptionHandler(InvalidMemberRoleException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidMemberRoleException e) {
        log.error("InvalidMemberRoleException", e);
        ErrorResponse response = new ErrorResponse(ErrorCode.INVALID_TOKEN);
        return new ResponseEntity<>(response, HttpStatusCode.valueOf(401));
    }
}
