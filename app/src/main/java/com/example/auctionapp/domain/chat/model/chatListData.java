package com.example.auctionapp.domain.chat.model;

public class chatListData {
    private Long itemId;
    private String profileName;
    private String chatTime;
    private String lastChat;

    public chatListData(){ }

    public chatListData(Long itemId, String profileName, String chatTime, String lastChat){
        this.itemId = itemId;
        this.profileName = profileName;
        this.chatTime = chatTime;
        this.lastChat = lastChat;
    }
    public Long getItemId() {
        return this.itemId;
    }
    public String getProfileName(){
        return this.profileName;
    }
    public String getChatTime(){
        return this.chatTime;
    }
    public String getLastChat(){
        return this.lastChat;
    }
}
