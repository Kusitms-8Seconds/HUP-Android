package com.example.auctionapp.domain.item.dto;

import com.mobsandgeeks.saripaar.annotation.NotEmpty;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RegisterItemRequest {

    @NotEmpty
    private String itemName;
    @NotEmpty private String category;
    @NotEmpty private int initPrice;
    @NotEmpty private LocalDateTime buyDate;
    @NotEmpty
    private int itemStatePoint;
    private String description;
    @NotEmpty private LocalDateTime auctionClosingDate;

}