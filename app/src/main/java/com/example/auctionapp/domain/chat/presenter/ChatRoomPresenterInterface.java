package com.example.auctionapp.domain.chat.presenter;

public interface ChatRoomPresenterInterface {
    void init();
    void sendMsg();
    void insertUserInfo(Long chatUserId);
    void sendMsgToDataBase();
    void checkChatRoom();

//    void getDestUid(); //상대방 uid 하나(single) 읽기
//    void getMessageList(); //채팅 내용 읽어들임

    void exceptionToast(String TAG, int statusCode);
}
