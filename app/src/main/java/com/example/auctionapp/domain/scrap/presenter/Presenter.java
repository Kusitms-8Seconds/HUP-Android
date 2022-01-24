package com.example.auctionapp.domain.scrap.presenter;

public interface Presenter {
    void init();
    void getData();

    void exceptionToast(int statusCode);
}
