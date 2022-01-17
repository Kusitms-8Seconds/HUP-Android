package com.example.auctionapp.domain.chat.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ChatConstants {
//    public enum EChatId {
//        myuid,
//        destUid,
//        chatRoomUid,
//        itemId;
//
//        String text;
//        public String getText(){
//            return this.text;
//        }
//    }

    public enum EChatFirebase {
        firebaseUrl("https://auctionapp-f3805-default-rtdb.asia-southeast1.firebasedatabase.app/"),
        itemId("itemId"),
        chatrooms("chatrooms"),
        comments("comments"),
        User("User"),
        users("users"),
        slash("/"),
        message("message"),
        timestamp("timestamp"),
        ;


        String text;
        EChatFirebase(String text) { this.text = text; }
        public String getText() { return  this.text;}
    }

    public enum EChatCallback {
        rtSuccessResponse("retrofit success, idToken: "),
        rtFailResponse("onFailResponse"),
        rtConnectionFail("연결실패"),
        errorBody("errorBody");

        String text;
        EChatCallback(String text) { this.text = text; }
        public String getText() { return  this.text;}
    }

}
