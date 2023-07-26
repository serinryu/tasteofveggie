package com.serinryu.springproject.exception;

public class NotFoundBlogIdException extends RuntimeException{
    public NotFoundBlogIdException(String message){
        super(message); // 에러 전달
    }
}
