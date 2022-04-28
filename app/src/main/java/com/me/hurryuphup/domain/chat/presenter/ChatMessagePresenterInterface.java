package com.me.hurryuphup.domain.chat.presenter;

import org.json.JSONException;

import java.io.IOException;

public interface ChatMessagePresenterInterface {
    void init(Long chatRoomId, Long destUid, Long EndItemId) throws IOException, JSONException;
    void checkChatRoom();
}
