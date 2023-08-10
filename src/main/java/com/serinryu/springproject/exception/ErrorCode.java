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

    /*
    NOT_FOUND(404, "COMMON-ERR-404", "PAGE NOT FOUND"),
    BAD_REQUEST(400, "BAD-REQUEST-400", "BAD REQUEST"),
    UNAUTHORIZED(401, "UNAUTHORIZED-401", "UNAUTHORIZED"),
    FORBIDDEN(403, "FORBIDDEN-403", "FORBIDDEN"),
    INTERNAL_SERVER_ERROR(500, "INTERNAL-SERVER-ERROR-500", "INTERNAL SERVER ERROR"),
    ;

     */

    INVALID_DATA(400, "InvalidData", "Invalid Data"),
    INVALID_TOKEN(400, "InvalidToken", "Invalid Token"),
	UNAUTHORIZED(401, "Unauthorized", "인증된 사용자가 아닙니다."),
	UNAVAILABLE(401, "Unavailable", "회원가입이 완료되지 않은 사용자입니다."),
    FORBIDDEN(403, "Forbidden", "허용되지 않은 사용자입니다."),
	NOT_FOUND(404, "NotFound", "존재하지 않는 데이터입니다."),
	CONFLICT(409, "Conflict", "데이터가 충돌되었습니다."),
    INTERNAL_SERVER_ERROR(500, "Internal Error", "서버에 문제가 생겼습니다."),
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
