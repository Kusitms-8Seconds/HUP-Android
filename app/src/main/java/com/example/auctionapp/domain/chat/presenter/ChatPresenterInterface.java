package com.example.auctionapp.domain.chat.presenter;

public interface ChatPresenterInterface {
    void init();
    void getChatList();
    void setChatList(String chatRoomUid, String oppId, Long itemIdL);
}
