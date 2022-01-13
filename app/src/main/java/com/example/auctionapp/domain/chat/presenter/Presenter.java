package com.example.auctionapp.domain.chat.presenter;

public interface Presenter {
    void init();
    void getChatList();
    void setChatList(String chatRoomUid, String oppId, Long itemIdL);
}
