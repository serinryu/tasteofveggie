package com.serinryu.springproject.exception;

// 데이터베이스 연결 문제나 쿼리 실행 오류 등이 발생할 때 InternalServerErrorException
public class InternalServerErrorException extends RuntimeException{
    public InternalServerErrorException(String message) {
        super(message);
    }
}
