package com.serinryu.springproject.exception;

import lombok.Getter;

//@AllArgsConstructor
@Getter
public enum ErrorCode {

    /**
     * ******************************* Global Error CodeList ***************************************
     * HTTP Status Code
     * 400 : Bad Request
     * 401 : Unauthorized
     * 403 : Forbidden
     * 404 : Not Found
     * 500 : Internal Server Error
     * *********************************************************************************************
     */

    // 서버로 요청한 리소스가 존재하지 않음
    NOT_FOUND(404, "COMMON-ERR-404", "PAGE NOT FOUND"),
    BAD_REQUEST(400, "BAD-REQUEST-400", "BAD REQUEST"),
    UNAUTHORIZED(401, "UNAUTHORIZED-401", "UNAUTHORIZED"),
    FORBIDDEN(403, "FORBIDDEN-403", "FORBIDDEN"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL-SERVER-ERROR-500", "INTERNAL SERVER ERROR"),
    ;


    /**
     * ******************************* Error Code Constructor ***************************************
     */

    private final int status;
    private final String code;
    private final String message;

    ErrorCode(final int status, final String code, final String message){
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
