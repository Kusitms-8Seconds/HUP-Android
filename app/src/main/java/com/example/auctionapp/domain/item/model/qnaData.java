package com.example.auctionapp.domain.item.model;

public class qnaData{
    private String title;
    private String date;
    private String id;
    private boolean state;  //true - 답변완료, false - 답변예정
    private boolean isFolded;

    public qnaData(){

    }

    public qnaData(String title, String date, String id, boolean state, boolean isFolded){
        this.title = title;
        this.date = date;
        this.id = id;
        this.state = state;
        this.isFolded = isFolded;
    }
    public String getTitle() {
        return this.title;
    }
    public String getDate(){
        return this.date;
    }
    public String getId(){
        return this.id;
    }
    public Boolean getState(){
        return this.state;
    }
    public Boolean getFolded(){
        return this.isFolded;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public void setId(String id) {
        this.id = id;
    }
    public void setState(Boolean state) {
        this.state = state;
    }
    public void setFolded(Boolean isFolded) {
        this.isFolded = isFolded;
    }

}