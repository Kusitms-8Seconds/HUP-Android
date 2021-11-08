package com.example.auctionapp;

public class ItemData {
    int imageURL;       //나중에 수정 (int -> string url)
    String itemName;
    int itemPrice;
    String endTime;
    int views;
    int heart;

    public ItemData(int imageURL, String itemName, int itemPrice, String endTime, int views, int heart){
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.endTime = endTime;
        this.views = views;
        this.heart = heart;
    }

    public int getImage() {
        return imageURL;
    }

    public void setImage(int imageURL) {
        this.imageURL = imageURL;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }
    public int getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }
    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }
    public int getHeart() {
        return heart;
    }

    public void setHeart(int heart) {
        this.heart = heart;
    }
}