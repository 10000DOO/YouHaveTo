package com.yht.exerciseassist.exception.error;

import lombok.Getter;

@Getter
public enum ErrorCode {
    NOT_FOUND_LOGIN_ID("이미 존재하는 아이디입니다."),
    NOT_FOUND_EMAIL("이미 존재하는 이메일입니다."),
    NOT_FOUND_USERNAME("이미 존재하는 이름입니다."),
    AUTHENTICATION_EXCEPTION("로그인 실패입니다. 다시 시도해주세요."),
    NOT_FOUND_EXCEPTION_DIARY("존재하지 않는 다이어리입니다."),
    NOT_FOUND_EXCEPTION_POST("존재하지 않는 게시글입니다."),
    NOT_FOUND_EXCEPTION_MEDIA("존재하지 않는 미디어입니다."),
    NOT_FOUND_EXCEPTION_MEMBER("존재하지 않는 유저입니다."),
    NOT_FOUND_EXCEPTION_COMMENT("존재하지 않는 댓글입니다."),
    IO_FAIL_EXCEOPTION("I/O 서버 오류입니다."),
    WRONG_CONTENT_TYPE("잘못된 Content-Type입니다."),
    NOT_EXIST_SAME_POST_IN_PARENT_AND_CHILD_COMMENT("원댓글과 같은 게시글에 존재하지 않습니다."),
    WRONG_TOKEN("잘못된 토큰입니다. 토큰 재발급이 불가능하니 다시 로그인 부탁드립니다."),
    NO_EXIST_TOKEN("존재하지 않는 토큰입니다. 토큰 재발급이 불가능하니 다시 로그인 부탁드립니다."),
    EXPIRED_TOKEN("토큰이 만료 되었습니다. 다시 로그인 해주세요."),
    NOT_LIKE_PRESSED("좋아요 정보가 없습니다."),
    ALREADY_PRESSED("이미 좋아요를 누르셨습니다."),
    DATE_FORMAT_EXCEPTION("날짜 형식 변환에 실패하였습니다."),
    NO_MATCHED_POST_TYPE("잘못된 게시글 타입입니다."),
    WRONG_EMAIL_CODE("인증 코드가 틀렸거나 시간이 초과되었습니다."),
    NOT_MINE_POST("본인 게시글이 아닙니다."),
    NOT_MINE_COMMENT("본인 댓글이 아닙니다."),
    NO_MATCHED_EXERCISE_CATEGORY("잘못된 운동 카테고리입니다."),
    NO_MATCHED_COMMENT("본인이 작성한 댓글만 삭제할 수 있습니다."),
    NO_MATCHED_POST("본인이 작성한 게시글만 삭제할 수 있습니다."),
    NO_ACCUSE_MINE_COMMENT("본인이 작성한 댓글은 신고할 수 없습니다."),
    NO_ACCUSE_MINE_POST("본인이 작성한 게시글은 신고할 수 없습니다."),
    ALREADY_HAVE_PARENTCOMMENT("해당 댓글에는 대댓글을 작성하실 수 없습니다."),
    WRONG_SEND_EMAIL("이메일 전송에 실패했습니다."),
    WRONG_EMAIL("이메일을 다시 확인해주세요."),
    NOT_FOUND_EXCEPTION_ACCUSE("존재하지 않는 신고입니다."),
    JSON_PARSE_EXCEPTION("JSON으로 변환 실패하였습니다."),
    FAIL_PW_AUTHENTICATION("비밀번호 인증에 실패하였습니다."),
    FAIL_EDIT_MEMBER_DATA("본인만 수정할 수 있습니다.");
    private final String message;

    ErrorCode(String message) {
        this.message = message;
    }
}
