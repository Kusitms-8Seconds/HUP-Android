package com.example.auctionapp.domain.mypage.constant;

public class MypageConstants {
    public enum ELogin {
        logout("로그아웃 되었습니다."),
        login("로그인하기"),
        afterLogin("로그인 후 이용 가능합니다.");

        String text;
        ELogin(String text) { this.text = text; }
        public String getText() { return this.text; }
    }
    public enum ELoginCallback {
        rtSuccessResponse("retrofit success, idToken: "),
        rtFailResponse("onFailResponse"),
        rtConnectionFail("연결실패");

        String text;
        ELoginCallback(String text) { this.text = text; }
        public String getText() { return  this.text;}
    }
}
