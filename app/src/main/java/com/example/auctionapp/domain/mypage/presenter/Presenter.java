package com.example.auctionapp.domain.mypage.presenter;

public interface Presenter {
    void init();
    void getUserInfo();
    void logout();
    void socialLogOut();
    void showToast(String message);

    void exceptionToast(String tag, int statusCode);
}
