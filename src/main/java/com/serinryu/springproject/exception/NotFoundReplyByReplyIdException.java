package com.serinryu.springproject.exception;

public class NotFoundReplyByReplyIdException extends RuntimeException{
    public NotFoundReplyByReplyIdException(String message){
        super(message);
    }
}
