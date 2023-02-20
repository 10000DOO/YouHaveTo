package com.yht.exerciseassist.exceoption.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NotFoundMember("이미 존재하는 아이디입니다. "),
    NotFoundEmail("이미 존재하는 이메일입니다. "),
    NotFoundUserName("이미 존재하는 이름입니다."),
    BadCredentialsException("비밀번호가 틀렸습니다. 다시 시도해주세요."),
    InternalAuthenticationServiceException("아이디가 틀렸습니다. 다시 시도해주세요."),
    AuthenticationException("로그인 실패입니다. 다시 시도해주세요."),
    NotFoundExceptionDiary("존재하지 않는 다이어리입니다."),
    NotFoundExceptionMedia("존재하지 않는 미디어입니다."),
    IllegalArgumentException("존재하지 않는 유저입니다."),
    DeleteFailedMediaException("미디어 파일 삭제 실패입니다. 다시 시도해주세요.");

    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
