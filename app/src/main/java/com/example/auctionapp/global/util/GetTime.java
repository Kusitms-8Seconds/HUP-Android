package com.example.auctionapp.global.util;


import android.graphics.Color;
import android.widget.TextView;

import com.example.auctionapp.databinding.ActivityBidPageBinding;
import com.example.auctionapp.databinding.ActivityItemDetailBinding;

import java.time.Month;
import java.util.Timer;

import lombok.Getter;

@Getter
public class GetTime {
    String latestTime;
    public GetTime(int year, Month monthM, String day, String hour, String min) {
        String month = getMonth(monthM.toString());
        if((year==2000) && month.equals("1") && day.equals("1") && hour.equals("1") && min.equals("1"))
            this.latestTime = "";
        else
            this.latestTime = month + "월 " + day + "일 " + hour + ":" + min;
    }
    public GetTime(String createdDate) {
        String month = createdDate.substring(5, 7);
//        if(month.charAt(0) == 0) month = String.valueOf(month.charAt(1));
        String day = createdDate.substring(8, 10);
        String time = createdDate.substring(11, 16);
        this.latestTime = month + "월 " + day + "일 " + time;
    }
    public GetTime(TextView leftTime, String days, String hours, String minutes, String second, Timer mTimer) {
        getLeftTime(leftTime, days, hours, minutes, second, mTimer);
    }
    public void getLeftTime(TextView leftTime, String days, String hours, String minutes, String second, Timer mTimer) {
        if(Integer.parseInt(hours) >= 24) {
            hours = String.valueOf(Integer.parseInt(hours)%24);
            leftTime.setText(days + "일 " + hours + "시간 " + minutes + "분 " + second + "초 전");
        } else if(Integer.parseInt(hours) < 0 || Integer.parseInt(minutes) < 0 || Integer.parseInt(second) < 0) {
            leftTime.setText("경매 시간 종료");
            mTimer.cancel();
        } else if(Integer.parseInt(hours) <= 0) {
            leftTime.setText(minutes + "분 " + second + "초 전");
            leftTime.setTextColor(Color.BLUE);
        } else if(Integer.parseInt(hours) <= 0 && Integer.parseInt(minutes) <= 0) {
            leftTime.setText(second + "초 전");
            leftTime.setTextColor(Color.BLUE);
        } else if(Integer.parseInt(hours) == 0 && Integer.parseInt(minutes) == 0
                && Integer.parseInt(second) > 0 &&  Integer.parseInt(second) < 20) {
            leftTime.setText(second + "초 전");
            leftTime.setTextColor(Color.BLUE);
        } else {
            leftTime.setText(hours + "시간 " + minutes + "분 " + second + "초 전");
        }
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
