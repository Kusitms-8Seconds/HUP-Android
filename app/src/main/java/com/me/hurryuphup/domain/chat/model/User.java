package com.me.hurryuphup.domain.chat.model;

public class User {
    public String name;
    public String profileImgUrl;
    public Long uid;

    public User() { }

    public User(String name, String profileImgUrl, Long uid) {
        this.name = name;
        this.profileImgUrl = profileImgUrl;
        this.uid = uid;
    }
}
