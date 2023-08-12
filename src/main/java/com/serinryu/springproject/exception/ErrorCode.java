package com.serinryu.springproject.exception;

import lombok.Getter;

//@AllArgsConstructor
@Getter
public enum ErrorCode {


    /**
     * ******************************* Global Error CodeList ***************************************
     */

    INVALID_REQUEST_PARAMETER(400, "BG-C-001", "유효하지 않은 요청 파라미터입니다."),
    INVALID_REQUEST_METHOD(405, "BG-C-002", "유효하지 않은 http 요청 메소드입니다."),
    INVALID_RESOURCE_OWNER(403, "BG-C-003", "해당 리소스를 처리할 권한이 없습니다."),
    SERVER_ERROR(500, "BG-S-001", "내부 서버 오류"),

    /**
     * ******************************* User Error CodeList ***************************************
     */

    DUPLICATE_USER_EMAIL(409, "US-C-001", "이미 사용 중인 이메일입니다."),
    INVALID_USER_PASSWORD(400, "US-C-002", "비밀번호가 일치하지 않습니다."),
    USER_NOT_FOUND(404, "US-C-003", "존재하지 않는 사용자입니다."),
    EMAIL_VERIFICATION_NOT_COMPLETED(403, "US-C-004", "이메일 인증이 완료되지 않았습니다."),

    /**
     * ******************************* Blog Error CodeList ***************************************
     */

    DUPLICATE_BLOG_TITLE(409, "BL-C-001", "이미 사용 중인 블로그 제목입니다."),
    INVALID_BLOG_CONTENT(400, "BL-C-002", "블로그 내용이 유효하지 않습니다."),
    BLOG_NOT_FOUND(404, "BL-C-003", "존재하지 않는 블로그입니다."),

    /**
     * ******************************* Reply Error CodeList ***************************************
     */

    DUPLICATE_REPLY_CONTENT(409, "RP-C-001", "이미 작성된 댓글 내용입니다."),
    INVALID_REPLY_CONTENT(400, "RP-C-002", "댓글 내용이 유효하지 않습니다."),
    REPLY_NOT_FOUND(404, "RP-C-003", "존재하지 않는 댓글입니다."),

    /**
     * ******************************* Auth Error CodeList ***************************************
     */

    INVALID_TOKEN(401, "AT-C-001", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(401, "AT-C-002", "만료된 토큰입니다."),
    NOT_EXPIRED_TOKEN(400, "AT-C-003", "만료되지 않은 토큰입니다."),
    REQUEST_TOKEN_NOT_FOUND(400, "AT-C-004", "요청에 토큰이 존재하지 않습니다."),
    INVALID_REFRESH_TOKEN(400, "AT-C-005", "유효하지 않은 리프레쉬 토큰입니다."),
    UNTRUSTED_CREDENTIAL(401, "AT-C-006", "신뢰할 수 없는 자격증명입니다."),
    LOGGED_OUT_TOKEN(401, "AT-C-007", "로그아웃된 토큰입니다."),
    INVALID_MEMBER_ROLE(403, "AT-C-100", "유효하지 않은 사용자 권한입니다."),
    NOT_AUTHORIZATION_USER(404, "AT-C-101", "인가된 사용자가 아닙니다."),
    MEMBER_PASSWORD_MISMATCH(401, "AT-C-102", "일치하지 않는 패스워드입니다."),
    AUTHENTICATION_ERROR(401, "AT-C-200", "Authentication exception."),
    INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION(500, "AT-S-200", "Internal authentication service exception.");


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
