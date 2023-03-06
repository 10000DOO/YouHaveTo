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
    IO_FAIL_EXCEOPTION("파일 I/O 서버 오류입니다."),
    WRONG_CONTENT_TYPE("잘못된 Content-Type입니다."),
    WRONG_TOKEN("잘못된 토큰입니다. 토큰 재발급이 불가능하니 다시 로그인 부탁드립니다."),
    NO_EXIST_TOKEN("존재하지 않는 토큰입니다. 토큰 재발급이 불가능하니 다시 로그인 부탁드립니다."),
    EXPIRED_TOKEN("토큰이 만료 되었습니다. 다시 로그인 해주세요."),
    NOT_LIKE_PRESSED("좋아요 정보가 없습니다."),
    ALREADY_PRESSED("이미 좋아요를 누르셨습니다."),
    DATE_FORMAT_EXCEPTION("날짜 형식 변환에 실패하였습니다."),
    NO_MATCHED_POST_TYPE("잘못된 게시글 타입입니다."),
    NO_MATCHED_EXERCISE_CATEGORY("잘못된 운동 카테고리입니다.");
    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
