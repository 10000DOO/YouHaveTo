package com.yht.exerciseassist.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class EditMemberDto {

    private String currentUsername;

    @Size(max = 20, message = "유저 이름은 20글자까지 작성 가능합니다.")
    private String usernameToChange;

    @Email(message = "잘못된 형식의 이메일입니다.")
    private String email;

    private String currentPassword;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[-_.~!@<>()$*?])[A-Za-z\\d-_.~!@<>()$*?]{8,20}$",
            message = "비밀번호는 영문 대 소문자, 숫자, 특수문자(-_.~!@<>()$*?)를 사용하세요. 비밀번호는 8~20자 입니다.")
    private String passwordToChange;

    private String field;
}
