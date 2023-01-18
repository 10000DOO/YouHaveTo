package com.yht.exerciseassist.domain.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class SignUpRequestDto {

    @NotBlank(message = "아이디는 필수 입니다.")
    @Length(min = 5, max = 20, message = "아이디는 5~20자 입니다.")
    private String loginId;

    @NotBlank(message = "비밀번호는 필수 입니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[$@$!%*?&])[A-Za-z\\d$@$!%*?&]{8,20}",
            message = "비밀번호는 영문 대 소문자, 숫자, 특수문자($@$!%*?&)를 사용하세요. 비밀번호는 8~20자 입니다.")
    private String password;

    @NotBlank(message = "사용자명은 필수입니다.")
    private String username;

    @NotBlank(message = "이메일은 필수 입니다.")
    @Email(message = "잘못된 형식의 이메일입니다.")
    private String email;

    private String field;//활동지역
}
