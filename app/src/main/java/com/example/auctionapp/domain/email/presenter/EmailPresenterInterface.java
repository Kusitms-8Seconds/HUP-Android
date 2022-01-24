package com.example.auctionapp.domain.email.presenter;

public interface EmailPresenterInterface {
    void sendEmail(String email);
    void checkCode(String code);
    void showToast(String message);

    void exceptionToast(int statusCode);
}
