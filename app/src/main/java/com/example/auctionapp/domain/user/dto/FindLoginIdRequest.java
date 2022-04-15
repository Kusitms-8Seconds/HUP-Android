package com.example.auctionapp.domain.user.dto;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FindLoginIdRequest {

    @NotEmpty(message = "회원의 가입한 이메일을 입력해주세요.")
//    @Email(message = "이메일 형식에 맞춰 입력해주세요.")
    private String email;

}
