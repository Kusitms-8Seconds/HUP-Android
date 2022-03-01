package com.example.auctionapp.domain.chat.model;

import java.util.HashMap;
import java.util.Map;

public class ChatModel {
    public Map<String, Boolean> users = new HashMap<>(); //채팅방 유저
    public Map<String, Long> itemId = new HashMap<>(); //채팅 아이템 id

    public static class Comment {
        public Long uid;
        public String message;
        public String timestamp;
        public int viewType;

        public Comment() {

        }

        public Comment(Long uid, String message, String timestamp, int viewType) {
            this.uid = uid;
            this.message = message;
            this.timestamp = timestamp;
            this.viewType = viewType;
        }

        public Long getUid() {
            return uid;
        }
        public String getMessage() {
            return message;
        }
        public String getTimestamp() {
            return timestamp;
        }
        public int getViewType() {
            return viewType;
        }
    }
}
