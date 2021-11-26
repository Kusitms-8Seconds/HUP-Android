package com.example.auctionapp.domain.user.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
public class Constants {

    public static Long userId = null;
    public static String token = null;
//    public static String imageBaseUrl = "http://192.168.1.2:8080/image/";
    public static String imageBaseUrl = "http://10.0.2.2:8080/image/";

    @Getter
    @NoArgsConstructor
    public enum ESignUp{
        emailDuplicateMessage("이미 사용하고 있는 이메일입니다."),
        emailNonDuplicateMessage("사용가능한 이메일입니다."),
        emailFormatErrorMessage("이메일을 알맞은 형태로 입력해주세요."),
        emailLetterCountError("이메일을 10~20자로 입력해주세요."),
        emailFormat("\\w+@\\w+\\.\\w+(\\.\\w+)?"),
        pwEnglishNumberFormat("^.*(?=^.{8,16}$)(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$"),
        pwInputMessage("비밀번호창에 비밀번호를 입력해주세요."),
        pwWarningMessage("비밀번호는 영어, 숫자, 특수문자를 조합해 8~12자리로 입력해주세요."),
        pwNotMatchMessage("비밀번호가 서로 일치하지 않습니다."),
        nameInputMessage("이름을 입력해주세요."),
        nameInput2Message("이름은 최소 2글자 이상 10글자 이하여야 합니다."),
        phoneNumberInputMessage("휴대폰번호를 입력해주세요,"),
        phoneNumberFormat("^\\d{3}-\\d{3,4}-\\d{4}$"),
        phoneNumberErrorMessage("올바른 휴대폰 번호를 입력해주세요."),
        genderCheckMessage("성별을 체크해주세요."),
        agreeCheckMessage("약관에 동의해주세요."),
        completeSignUpMessage("회원가입을 완료했습니다."),
        dpYear("년"),
        dpMonth("월"),
        dpDay("일"),
        emailAppType("APP"),
        userEmail("userEmail"),
        userPassword("userPassword");

        private String text;
        private ESignUp(String text){
            this.text=text;
        }
        public String getText(){
            return this.text;
        }

    }
}
