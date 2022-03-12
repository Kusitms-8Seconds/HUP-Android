package com.example.auctionapp.global.util;


import java.time.Month;
import lombok.Getter;

@Getter
public class GetChatTime {
    String latestTime;
    public GetChatTime(Month monthM, String day, String hour, String min) {
        String month = getMonth(monthM.toString());
        this.latestTime = month + "월 " + day + "일 " + hour + ":" + min;
    }
    public GetChatTime(String createdDate) {
        String month = createdDate.substring(5, 7);
//        if(month.charAt(0) == 0) month = String.valueOf(month.charAt(1));
        String day = createdDate.substring(8, 10);
        String time = createdDate.substring(11, 16);
        this.latestTime = month + "월 " + day + "일 " + time;
    }
    public String getMonth(String monthStr) {
        String str = "";
        switch (monthStr) {
            case "JANUARY":
                str = "1";
                break;
            case "FEBRUARY":
                str = "2";
                break;
            case "MARCH":
                str = "3";
                break;
            case "APRIL":
                str = "4";
                break;
            case "MAY":
                str = "5";
                break;
            case "JUNE":
                str = "6";
                break;
            case "JULY":
                str = "7";
                break;
            case "AUGUST":
                str = "8";
                break;
            case "SEPTEMBER":
                str = "9";
                break;
            case "OCTOBER":
                str = "10";
                break;
            case "NOVEMBER":
                str = "11";
                break;
            case "DECEMBER":
                str = "12";
                break;
        }
        return str;
    }
}
