package com.example.auctionapp.global.retrofit;

import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.dto.DefaultResponse;
import com.example.auctionapp.domain.item.dto.ImageResponse;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.dto.RegisterItemRequest;
import com.example.auctionapp.domain.item.dto.RegisterItemResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapCountResponse;
import com.example.auctionapp.domain.user.dto.LoginRequest;
import com.example.auctionapp.domain.user.dto.OAuth2GoogleLoginRequest;
import com.example.auctionapp.domain.user.dto.LoginResponse;
import com.example.auctionapp.domain.user.dto.OAuth2KakaoLoginRequest;
import com.example.auctionapp.domain.user.dto.OAuth2NaverLoginRequest;
import com.example.auctionapp.domain.user.dto.SignUpRequest;
import com.example.auctionapp.domain.user.dto.SignUpResponse;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoRequest;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoResponse;
import com.example.auctionapp.global.dto.PaginationDto;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Streaming;

public interface RestAPI {

    @POST("signup")
    Call<SignUpResponse> signup(@Body SignUpRequest signUpRequest);
    @POST("login")
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("api/v1/user/details")
    Call<UserDetailsInfoResponse> userDetails(@Body UserDetailsInfoRequest userDetailsInfoRequest);
    @POST("oauth2/google/validation")
    Call<LoginResponse> googleIdTokenValidation(@Body OAuth2GoogleLoginRequest oAuth2GoogleLoginRequest);
    @POST("oauth2/kakao/validation")
    Call<LoginResponse> kakaoAccessTokenValidation(@Body OAuth2KakaoLoginRequest oAuth2KakaoLoginRequest);
    @POST("oauth2/naver/validation")
    Call<LoginResponse> naverAccessTokenValidation(@Body OAuth2NaverLoginRequest oAuth2NaverLoginRequest);
    @GET("api/v1/items/list/status/{itemSoldStatus}")
    Call<PaginationDto<List<ItemDetailsResponse>>> getAllItemsInfo(@Path("itemSoldStatus") ItemConstants.EItemSoldStatus itemSoldStatus);
    @GET("api/v1/scrap/heart/{id}")
    Call<ScrapCountResponse> getHeart(@Path("id") Long id);
    //scrap
    @POST("api/v1/scrap/{id}")
    Call<DefaultResponse> scrapItem(@Path("id") Long id);

    @Multipart
    @POST("/api/v1/items")
    Call<RegisterItemResponse> uploadItem(@Part List<MultipartBody.Part> fileNames,
                                          @Part("itemName") RequestBody itemName,
                                          @Part("category") RequestBody category,
                                          @Part("initPrice") RequestBody initPrice,
                                          @Part("buyDate") RequestBody buyDate,
                                          @Part("itemStatePoint") RequestBody itemStatePoint,
                                          @Part("auctionClosingDate") RequestBody auctionClosingDate,
                                          @Part("description") RequestBody description);
    /*
    map.put("category", categoryR);
                map.put("initPrice", initPriceR);
                map.put("buyDate", buyDateR);
                map.put("itemStatePoint", itemStatePointR);
                map.put("auctionClosingDate", auctionClosingDateR);
                map.put("description", descriptionR);
     */
    @DELETE("/api/v1/items/{id}")
    Call<DefaultResponse> deleteItem(@Path("id") Long id);
}