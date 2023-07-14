package com.yht.exerciseassist.domain.member.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditMemberDto {

    @Size(max = 20, message = "유저 이름은 20글자까지 작성 가능합니다.")
    private String username;

    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[-_.~!@<>()$*?])[A-Za-z\\d-_.~!@<>()$*?]{8,20}$",
            message = "비밀번호는 영문 대 소문자, 숫자, 특수문자(-_.~!@<>()$*?)를 사용하세요. 비밀번호는 8~20자 입니다.")
    private String password;

    private String field;

    private Long memberId;
}
