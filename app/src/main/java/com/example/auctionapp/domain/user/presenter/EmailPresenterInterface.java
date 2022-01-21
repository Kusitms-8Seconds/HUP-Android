package com.example.auctionapp.domain.user.presenter;

public interface EmailPresenterInterface {
    void sendEmail(String email);
    void checkCode(String code);
    void showToast(String message);
}
