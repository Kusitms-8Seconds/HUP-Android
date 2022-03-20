package com.example.auctionapp.global.retrofit;

import androidx.annotation.Nullable;

import com.example.auctionapp.domain.chat.dto.ChatMessageResponse;
import com.example.auctionapp.domain.chat.dto.ChatRoomResponse;
import com.example.auctionapp.domain.chat.dto.IsEnterChatRoomRequest;
import com.example.auctionapp.domain.chat.dto.IsEnterChatRoomResponse;
import com.example.auctionapp.domain.chat.view.ChatMessage;
import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.dto.BestItemResponse;
import com.example.auctionapp.domain.mypage.notice.dto.NoticeListResponse;
import com.example.auctionapp.domain.mypage.notice.dto.NoticeResponse;
import com.example.auctionapp.domain.mypage.notice.dto.UpdateNoticeResponse;
import com.example.auctionapp.domain.notification.dto.NotificationListResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.PriceSuggestionRequest;
import com.example.auctionapp.domain.pricesuggestion.dto.PriceSuggestionResponse;
import com.example.auctionapp.domain.user.dto.LogoutRequest;
import com.example.auctionapp.domain.user.dto.TokenInfoResponse;
import com.example.auctionapp.domain.user.dto.UpdateProfileImgRequest;
import com.example.auctionapp.domain.user.dto.UpdateProfileResponse;
import com.example.auctionapp.domain.user.dto.UpdateUserRequest;
import com.example.auctionapp.domain.user.dto.UpdateUserResponse;
import com.example.auctionapp.global.dto.DefaultResponse;
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
import com.example.auctionapp.domain.email.dto.CheckAuthCodeRequest;
import com.example.auctionapp.domain.email.dto.EmailAuthCodeRequest;
import com.example.auctionapp.domain.user.dto.LoginRequest;
import com.example.auctionapp.domain.user.dto.OAuth2GoogleLoginRequest;
import com.example.auctionapp.domain.user.dto.LoginResponse;
import com.example.auctionapp.domain.user.dto.OAuth2KakaoLoginRequest;
import com.example.auctionapp.domain.user.dto.OAuth2NaverLoginRequest;
import com.example.auctionapp.domain.user.dto.SignUpRequest;
import com.example.auctionapp.domain.user.dto.SignUpResponse;
import com.example.auctionapp.domain.user.dto.UserInfoResponse;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.firebase.FCMRequest;
import com.example.auctionapp.global.firebase.FCMResponse;
import com.kakao.usermgmt.request.UpdateProfileRequest;

import java.io.File;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RestAPI {

    @POST("api/v1/users")   //사용자 생성
    Call<SignUpResponse> signup(@Body SignUpRequest signUpRequest);
    @PUT("api/v1/users")    //사용자 정보 수정
    Call<UpdateUserResponse> updateUser(@Body UpdateUserRequest updateUserRequest);
    @Multipart
    @PUT("api/v1/users/images")    //사용자 프로필 사진 수정
    Call<UpdateProfileResponse> updateUserProfileImg(@Part MultipartBody.Part file,
                                                     @Part("userId") RequestBody userId);
    @POST("api/v1/email/send")   //회원가입 시 이메일 인증
    Call<DefaultResponse> sendAuthCode(@Body EmailAuthCodeRequest emailAuthCodeRequest);
    @POST("api/v1/email/verify")   //인증코드 확인
    Call<DefaultResponse> checkAuthCode(@Body CheckAuthCodeRequest checkAuthCodeRequest);

    @POST("api/v1/users/login") //로그인
    Call<LoginResponse> login(@Body LoginRequest loginRequest);
    @POST("api/v1/users/login") //로그아웃
    Call<DefaultResponse> logout(@Body LogoutRequest logoutRequest);
    @POST("api/v1/users/reissue") //토큰 재발급
    Call<TokenInfoResponse> reissue(@Body LogoutRequest logoutRequest);
    @GET("api/v1/users/{id}")      //사용자 정보 조회
    Call<UserInfoResponse> userDetails(@Path("id") Long userId);
    @POST("api/v1/users/google-login")      //구글 로그인
    Call<LoginResponse> googleIdTokenValidation(@Body OAuth2GoogleLoginRequest oAuth2GoogleLoginRequest);
    @POST("api/v1/users/kakao-login")       //카카오 로그인
    Call<LoginResponse> kakaoAccessTokenValidation(@Body OAuth2KakaoLoginRequest oAuth2KakaoLoginRequest);
    @POST("api/v1/users/naver-login")       //네이버 로그인
    Call<LoginResponse> naverAccessTokenValidation(@Body OAuth2NaverLoginRequest oAuth2NaverLoginRequest);
    @GET("api/v1/users/check/{loginId}")       //아이디 중복체크
    Call<DefaultResponse> checkDuplicateId(@Path("loginId") String loginId);

    @Multipart
    @POST("api/v1/items")   //아이템 생성
    Call<RegisterItemResponse> uploadItem(@Part List<MultipartBody.Part> files,
                                          @Part("userId") RequestBody userId,
                                          @Part("itemName") RequestBody itemName,
                                          @Part("category") RequestBody category,
                                          @Part("initPrice") RequestBody initPrice,
                                          @Part("buyDate") RequestBody buyDate,
                                          @Part("itemStatePoint") RequestBody itemStatePoint,
                                          @Part("auctionClosingDate") RequestBody auctionClosingDate,
                                          @Part("description") RequestBody description);
    @GET("api/v1/items/{id}")   //아이템 조회
    Call<ItemDetailsResponse> getItem(@Path("id") Long id);
    @GET("api/v1/items/statuses/{itemSoldStatus}")     //아이템 판매상태별 조회
    Call<PaginationDto<List<ItemDetailsResponse>>> getAllItemsInfo(@Path("itemSoldStatus") ItemConstants.EItemSoldStatus itemSoldStatus);
    @GET("api/v1/items/statuses/hearts/{itemSoldStatus}")  //아이템 판매상태별, 스크랩 많은 순 조회
    Call<List<BestItemResponse>> getBestItems(@Path("itemSoldStatus") ItemConstants.EItemSoldStatus itemSoldStatus);
    @GET("api/v1/items/categories/{category}")      //아이템 카테고리별 조회
    Call<PaginationDto<List<ItemDetailsResponse>>> getAllItemsByCategory(@Path("category") String category);
    @POST("api/v1/items/users")      //유저가 등록한 상품을 상태별로 조회
    Call<PaginationDto<List<ItemDetailsResponse>>> getAllItemsByUserIdAndStatus(@Body GetAllItemsByStatusRequest getAllItemsByStatusRequest);
    @DELETE("/api/v1/items/{id}")   //아이템 삭제
    Call<DefaultResponse> deleteItem(@Path("id") Long id);

    @POST("api/v1/scraps")   //scrap create
    Call<ScrapRegisterResponse> createScrap(@Body ScrapRegisterRequest scrapRegisterRequest);
    @DELETE("api/v1/scraps/{scrapId}")   //scrap delete
    Call<DefaultResponse> deleteHeart(@Path("scrapId") Long scrapId);
    @GET("api/v1/scraps/hearts/{itemId}")     //상품의 좋아요 수 조회
    Call<ScrapCountResponse> getHeart(@Path("itemId") Long itemId);
    @POST("api/v1/scraps/hearts")   //유저가 해당 상품을 스크랩 중인 지 조회
    Call<ScrapCheckedResponse> isCheckedHeart(@Body ScrapCheckedRequest scrapCheckedRequest);
    @GET("api/v1/scraps/users/{userId}")      //유저의 스크랩 내역 조회
    Call<PaginationDto<List<ScrapDetailsResponse>>> getAllScrapsByUserId(@Path("userId") Long userId);

    @GET("api/v1/priceSuggestions/{itemId}")   //해당 상품의 모든 입찰내역 조회
    Call<PaginationDto<List<PriceSuggestionListResponse>>> getAllPriceSuggestionByItemId(@Path("itemId") Long itemId);
    @GET("api/v1/priceSuggestions/bidders/{itemId}")      //해당 상품의 최고입찰 유저 조회
    Call<BidderResponse> getBidder(@Path("itemId") Long itemId);
    @GET("api/v1/priceSuggestions/maximumPrice/{itemId}")    //해당 상품의 최고입찰가격 조회
    Call<MaximumPriceResponse> getMaximumPrice(@Path("itemId") Long itemId);
    @GET("api/v1/priceSuggestions/participants/{itemId}")     //해당 상품의 참여자수 조회
    Call<ParticipantsResponse> getParticipants(@Path("itemId") Long itemId);
    @GET("api/v1/priceSuggestions/users/{userId}")   //해당 유저의 경매 참여내역 조회
    Call<PaginationDto<List<PriceSuggestionListResponse>>> getAllPriceSuggestionByUserId(@Path("userId") Long userId);
    @POST("api/v1/priceSuggestions/priceSuggestionTest")   //입찰하기
    Call<PriceSuggestionResponse> priceSuggest(@Body PriceSuggestionRequest priceSuggestionRequest);

    //FCM
    @POST("api/v1/items/sold")   //낙찰하기
    Call<FCMResponse> pushMessage(@Body FCMRequest fcmRequest);
    //notification
    @GET("api/v1/notifications/{userId}")     //유저의 모든 알림 조회
    Call<PaginationDto<List<NotificationListResponse>>> getNotificationList(@Path("userId") Long id,
                                                                            @Query("page") int page, @Query("size") int size);

    //공지사항
    @GET("api/v1/notices")     //공지사항 전체 목록 조회
    Call<PaginationDto<List<NoticeListResponse>>> getAllNotice();
    @Multipart
    @PUT("api/v1/notices")     //공지사항 수정
    Call<UpdateNoticeResponse> updateNotice(@Part List<MultipartBody.Part> files,
                                            @Part("title") RequestBody title,
                                            @Part("body") RequestBody body,
                                            @Part("userId") RequestBody userId,
                                            @Part("noticeId") RequestBody noticeId);
    @Multipart
    @POST("api/v1/notices")     //공지사항 등록
    Call<NoticeResponse> uploadNotice(@Nullable @Part List<MultipartBody.Part> files,
                                      @Part("title") RequestBody title,
                                      @Part("body") RequestBody body,
                                      @Part("userId") RequestBody userId);
    @GET("api/v1/notices/{noticeId}")     //공지사항 상세 조회
    Call<NoticeResponse> getNotice(@Path("noticeId") Long noticeId);
    @DELETE("api/v1/notices/{noticeId}")     //공지사항 삭제
    Call<DefaultResponse> deleteNotice(@Path("noticeId") Long noticeId);

    //chat
    @GET("api/v1/chatRooms/{id}")     //유저의 모든 채팅방 조회
    Call<List<ChatRoomResponse>> getChatRooms(@Path("id") Long id);
    @GET("api/v1/chatMessages/{id}")     //채팅방의 모든 메세지 조회
    Call<PaginationDto<List<ChatMessageResponse>>> getChatMessages(@Path("id") Long id,
                                                                   @Query("page") int page, @Query("size") int size);
    @POST("api/v1/chatRooms/isEnter")   //해당 유저의 채팅방 입장여부 조회
    Call<IsEnterChatRoomResponse> isChatRoomEntered(@Body IsEnterChatRoomRequest isEnterChatRoomRequest);
}