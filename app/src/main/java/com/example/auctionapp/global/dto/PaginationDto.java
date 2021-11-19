package com.example.auctionapp.global.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaginationDto<T> {

    private int totalPage;
    private int currentPage;
    private Long totalElements;
    private int currentElements;
    private boolean hasPrevious;
    private boolean hasNext;
    private boolean isLast;
    private T data;

}