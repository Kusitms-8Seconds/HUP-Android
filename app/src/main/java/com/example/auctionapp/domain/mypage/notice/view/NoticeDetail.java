package com.example.auctionapp.domain.mypage.notice.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.auctionapp.databinding.ActivityNoticeDetailBinding;
import com.example.auctionapp.domain.mypage.constant.MypageConstants;
import com.example.auctionapp.domain.mypage.notice.constant.NoticeConstants;
import com.example.auctionapp.domain.mypage.notice.dto.NoticeListResponse;
import com.example.auctionapp.domain.mypage.notice.dto.NoticeResponse;
import com.example.auctionapp.domain.mypage.notice.model.NoticeData;
import com.example.auctionapp.domain.mypage.notice.presenter.NoticePresenter;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.dto.DefaultResponse;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.util.ErrorMessageParser;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class NoticeDetail extends AppCompatActivity {
    private ActivityNoticeDetailBinding binding;

    Long noticeId;
    String title;
    String body;
    String userName;

/*
<sample>
안녕하세요, HUP!입니다.

더욱 발전된 서비스 제공을 위해 어플리케이션 점검이 진행될 예정입니다. 서비스 점검 시간에는 어플 사용이 잠시 중단됩니다. 사용자 여러분들의 양해를 부탁드립니다.
해당시간 내에 문의 사항이 있으실 경우 HUP! 고객센터 1600-9494로 전화 부탁드립니다.

점검일시
2021년 11월 26일(금) 오전 3시 - 5시

업데이트 시간은 상황에 따라 단축 또는 연장될 수 있습니다.
 */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticeDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        noticeId = intent.getLongExtra("noticeId", 0);

        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getNotice(noticeId)
                .enqueue(MainRetrofitTool.getCallback(new getNoticeDetailCallback()));

        binding.goback.bringToFront();
        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        //관리자일때 (admin)
        if(Constants.userId == null || Constants.userId != 100) {
            binding.deleteButton.setVisibility(View.GONE);
            binding.goUpdateNotice.setVisibility(View.GONE);
        }
        else if(Constants.userId == 100) {
            binding.deleteButton.setVisibility(View.VISIBLE);
            binding.goUpdateNotice.setVisibility(View.VISIBLE);
        }
        //delete button
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).deleteNotice(noticeId)
                        .enqueue(MainRetrofitTool.getCallback(new deleteNoticeCallback()));
                Intent intent = new Intent(getApplicationContext(), Notice.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        //수정 button
        binding.goUpdateNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), UpdateNotice.class);
                intent.putExtra("noticeId", noticeId);
                intent.putExtra("title", title);
                intent.putExtra("body", body);
                intent.putExtra("userName", userName);
                startActivity(intent);
            }
        });
    }
    public class getNoticeDetailCallback implements MainRetrofitCallback<NoticeResponse> {
        @Override
        public void onSuccessResponse(Response<NoticeResponse> response) {
            title = response.body().getTitle();
            body = response.body().getBody();
            userName = response.body().getUserName();
            binding.noticeTitle.setText(title);
            binding.userName.setText(userName);
            binding.noticeContent.setText(body);
            String noticeImgUrl = Constants.imageBaseUrl;
            if(!response.body().getFileNames().isEmpty())
                noticeImgUrl += response.body().getFileNames().get(0);
            else binding.noticeImg.setVisibility(View.GONE);
            Glide.with(getApplicationContext()).load(noticeImgUrl).override(binding.noticeImg.getWidth()
                    ,binding.noticeImg.getHeight()).into(binding.noticeImg);
            Log.d(TAG, MypageConstants.EMyPageCallback.rtSuccessResponse.getText() + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<NoticeResponse> response) throws IOException, JSONException {
//            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
//            noticeView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, MypageConstants.EMyPageCallback.rtFailResponse.getText() + ":"+response.errorBody().string());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e( MypageConstants.EMyPageCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
    public class deleteNoticeCallback implements MainRetrofitCallback<DefaultResponse> {
        @Override
        public void onSuccessResponse(Response<DefaultResponse> response) {
            showToast("삭제 완료");
            Log.d(TAG, "delete notice_on success");
        }
        @Override
        public void onFailResponse(Response<DefaultResponse> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
            showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, MypageConstants.EMyPageCallback.rtFailResponse.getText() + ":"+response.errorBody().string());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e( MypageConstants.EMyPageCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
