package com.me.hurryuphup.domain.user.dto;

import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpRequest {

    @NotEmpty(message = "회원의 회원가입Id를 입력해주세요.")
    @Length(min = 5, max = 11, message = "회원가입 Id는 크기가 5에서 11사이여야 합니다.")
    private String loginId;

    @NotEmpty(message = "회원의 이메일을 입력해주세요.")
    @Email(message = "이메일 형식에 맞춰 입력해주세요.")
    private String email;

    @NotEmpty(message = "회원 이름을 입력해 주세요.")
    @Length(min = 2, max = 10, message = "이름은 2글자 이상 10글자 이하여야 합니다.")
    private String username;

    @NotEmpty(message = "회원의 비밀번호를 입력해 주세요.")
    @Length(min = 8, max = 16, message = "패스워드는 8글자 이상 16글자 이하여야 합니다.")
    private String password;

    @NotEmpty(message = "전화번호를 입력해주세요.")
    private String phoneNumber;

}
