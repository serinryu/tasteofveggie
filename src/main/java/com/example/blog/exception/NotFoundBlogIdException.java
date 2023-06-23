package com.example.blog.exception;

// RuntimeException 상속받아서 Unchecked Exception 으로 만들어줌 (요새는 Checked Exception 지양)
public class NotFoundBlogIdException extends RuntimeException{
    public NotFoundBlogIdException(String message){
        super(message); // 에러 전달
    }
}
