package com.example.auctionapp.global.retrofit;

import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.dto.BestItemResponse;
import com.example.auctionapp.domain.item.dto.DefaultResponse;
import com.example.auctionapp.domain.item.dto.GetAllItemsByStatusRequest;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.dto.RegisterItemResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.BidderResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.ParticipantsResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.PriceSuggestionListResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapCheckedRequest;
import com.example.auctionapp.domain.scrap.dto.ScrapCheckedResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapCountResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapDetailsResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapRegisterRequest;
import com.example.auctionapp.domain.scrap.dto.ScrapRegisterResponse;
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

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

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
    @GET("api/v1/items/list/status/heart/{itemSoldStatus}")
    Call<List<BestItemResponse>> getBestItems(@Path("itemSoldStatus") ItemConstants.EItemSoldStatus itemSoldStatus);
    @GET("api/v1/scrap/heart/{id}")
    Call<ScrapCountResponse> getHeart(@Path("id") Long id);
    @POST("api/v1/scrap/{id}")
    Call<DefaultResponse> scrapItem(@Path("id") Long id);
    @Multipart
    @POST("api/v1/items")
    Call<RegisterItemResponse> uploadItem(@Part List<MultipartBody.Part> files,
                                          @Part("userId") RequestBody userId,
                                          @Part("itemName") RequestBody itemName,
                                          @Part("category") RequestBody category,
                                          @Part("initPrice") RequestBody initPrice,
                                          @Part("buyDate") RequestBody buyDate,
                                          @Part("itemStatePoint") RequestBody itemStatePoint,
                                          @Part("auctionClosingDate") RequestBody auctionClosingDate,
                                          @Part("description") RequestBody description);

    @GET("api/v1/priceSuggestion/maximumPrice/{itemId}")
    Call<MaximumPriceResponse> getMaximumPrice(@Path("itemId") Long itemId);
    @GET("api/v1/priceSuggestion/participant/{itemId}")
    Call<ParticipantsResponse> getParticipants(@Path("itemId") Long itemId);
    @POST("api/v1/scrap")
    Call<ScrapRegisterResponse> createScrap(@Body ScrapRegisterRequest scrapRegisterRequest);
    @POST("api/v1/scrap/heart/check")
    Call<ScrapCheckedResponse> isCheckedHeart(@Body ScrapCheckedRequest scrapCheckedRequest);
    @DELETE("api/v1/scrap/{scrapId}")
    Call<DefaultResponse> deleteHeart(@Path("scrapId") Long scrapId);
    @GET("api/v1/scrap/list/{userId}")
    Call<PaginationDto<List<ScrapDetailsResponse>>> getAllScrapsByUserId(@Path("userId") Long userId);
    @GET("api/v1/items/{id}")
    Call<ItemDetailsResponse> getItem(@Path("id") Long id);
    @GET("api/v1/priceSuggestion/list/item/{id}")
    Call<PaginationDto<List<PriceSuggestionListResponse>>> getAllPriceSuggestionByItemId(@Path("id") Long id);
    @GET("api/v1/priceSuggestion/list/user/{id}")
    Call<PaginationDto<List<PriceSuggestionListResponse>>> getAllPriceSuggestionByUserId(@Path("id") Long id);
    @POST("api/v1/items/list/status")
    Call<PaginationDto<List<ItemDetailsResponse>>> getAllItemsByUserIdAndStatus(@Body GetAllItemsByStatusRequest getAllItemsByStatusRequest);
    @GET("api/v1/priceSuggestion/bidder/{itemId}")
    Call<BidderResponse> getBidder(@Path("itemId") Long itemId);
    @DELETE("/api/v1/items/{id}")
    Call<DefaultResponse> deleteItem(@Path("id") Long id);
}