package com.serinryu.springproject.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestControllerAdvice // 여러 컨트롤러에 대해 전역적으로 ExceptionHandler를 적용
public class GlobalExceptionHandler {

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleInternalServerError(InternalServerErrorException e) {
        log.error("InternalServerErrorException", e);
        ErrorResponse response = new ErrorResponse(ErrorCode.INTERNAL_SERVER_ERROR);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(InvalidDataException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrityViolation(InvalidDataException e) {
        log.error("InvalidDataException", e);
        ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorResponse> UnauthorizedHandler(UnauthorizedException e){
        log.error("UnauthorizedException", e);
        ErrorResponse response = new ErrorResponse(ErrorCode.UNAUTHORIZED);
        return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> ForbiddenHandler(ForbiddenException e){
        log.error("Forbidden", e);
        ErrorResponse response = new ErrorResponse(ErrorCode.FORBIDDEN);
        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotFoundBlogIdException.class)
    public ResponseEntity<ErrorResponse> NotFoundBlogIdHandler(NotFoundBlogIdException e){
        log.error("NotFoundBlogIdException", e);
        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NotFoundReplyByReplyIdException.class)
    public ResponseEntity<ErrorResponse> NotFoundReplyByReplyIdHandler(NotFoundReplyByReplyIdException e){
        log.error("NotFoundReplyByReplyIdException", e);
        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ErrorResponse> handleInvalidTokenException(InvalidTokenException e) {
        log.error("InvalidTokenException", e);
        ErrorResponse response = new ErrorResponse(ErrorCode.BAD_REQUEST);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /*
    @ExceptionHandler(NotFoundBlogIdException.class)
    public String NotFoundBlogIdHandler(NotFoundBlogIdException e, HttpServletRequest request, Model model){
        log.error("NotFoundBlogIdException", e);
        return "error/NotFoundBlogIdExceptionResultPage";
    }
    */
}
