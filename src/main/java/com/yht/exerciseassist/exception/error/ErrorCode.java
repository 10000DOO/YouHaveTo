package com.yht.exerciseassist.exception.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_FOUND_LOGIN_ID("이미 존재하는 아이디입니다."),
    NOT_FOUND_EMAIL("이미 존재하는 이메일입니다."),
    NOT_FOUND_USERNAME("이미 존재하는 이름입니다."),
    BAD_CREDENTIALS_EXCEPTION("비밀번호가 틀렸습니다. 다시 시도해주세요."),
    INTERNAL_AUTHENTICATION_SERVICE_EXCEPTION("아이디가 틀렸습니다. 다시 시도해주세요."),
    AUTHENTICATION_EXCEPTION("로그인 실패입니다. 다시 시도해주세요."),
    NOT_FOUND_EXCEPTION_DIARY("존재하지 않는 다이어리입니다."),
    NOT_FOUND_EXCEPTION_POST("존재하지 않는 게시글입니다."),
    NOT_FOUND_EXCEPTION_MEDIA("존재하지 않는 미디어입니다."),
    NOT_FOUND_EXCEPTION_MEMBER("존재하지 않는 유저입니다."),
    DELETE_FAILED_MEDIA_EXCEPTION("미디어 파일 삭제 실패입니다. 다시 시도해주세요."),
    IO_FAIL_EXCEOPTION("파일 입출력 실패입니다."),
    WRONG_CONTENT_TYPE("잘못된 Content-Type입니다.");
    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
