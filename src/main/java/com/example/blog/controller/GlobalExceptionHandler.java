package com.example.blog.controller;

import com.example.blog.exception.ErrorCode;
import com.example.blog.exception.ErrorResponse;
import com.example.blog.exception.NotFoundBlogIdException;
import com.example.blog.exception.NotFoundReplyByReplyIdException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundReplyByReplyIdException.class)
    public ResponseEntity<ErrorResponse> NotFoundReplyByReplyIdHandler(NotFoundReplyByReplyIdException e){
        log.error("NotFoundReplyByReplyIdException", e);
        ErrorResponse response = new ErrorResponse(ErrorCode.NOT_FOUND);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundBlogIdException.class)
    public String NotFoundBlogIdHandler(NotFoundBlogIdException e, HttpServletRequest request, Model model){
        log.error("NotFoundBlogIdException", e);
        return "error/NotFoundBlogIdExceptionResultPage";
    }
}
