package com.serinryu.springproject.exception;

public class InvalidMemberRoleException extends RuntimeException {
    public InvalidMemberRoleException(String message) {
        super(message);
    }
}
