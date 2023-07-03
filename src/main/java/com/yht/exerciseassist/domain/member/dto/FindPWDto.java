package com.yht.exerciseassist.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FindPWDto {

    @NotBlank(message = "비밀번호는 필수 입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[-_.~!@<>()$*?])[A-Za-z\\d-_.~!@<>()$*?]{8,20}$",
            message = "비밀번호는 영문 대 소문자, 숫자, 특수문자(-_.~!@<>()$*?)를 사용하세요. 비밀번호는 8~20자 입니다.")
    private String password;

    @NotBlank(message = "이메일은 필수 입니다.")
    @Email(message = "잘못된 형식의 이메일입니다.")
    private String email;
}
