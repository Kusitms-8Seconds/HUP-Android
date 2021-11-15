package com.example.auctionapp.global.retrofit;

import android.content.ClipData;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegisterItemResponse {

    private Long id;

    private final String itemName;
    private Enum category;
    private int initPrice;
    private LocalDateTime buyDate;
    private int itemStatePoint;
    private String description;
    private Enum soldStatus;
    private List<String> fileNames;
    private LocalDateTime auctionClosingDate;

}