package com.example.auctionapp.domain.user.dto;

import androidx.annotation.Size;

import com.mobsandgeeks.saripaar.annotation.Email;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SignUpRequest {

    @Length(min = 5, max = 11)
    private String loginId;

    @Email(message = "이메일 형식에 맞춰 입력해주세요.")
    private String email;

    @NotEmpty(message = "이름을 입력해주세요.")
    @Length(min = 2, max = 10, message = "이름은 최소 2글자 이상 10글자 이하여야 합니다.")
    private String username;

    @NotEmpty(message = "패스워드를 입력해주세요.")
    @Length(min = 8, max = 16, message = "패스워드는 8글자 이상 16글자 이하여야 합니다.")
    private String password;

    @NotEmpty(message = "전화번호를 입력해주세요.")
    private String phoneNumber;

}
