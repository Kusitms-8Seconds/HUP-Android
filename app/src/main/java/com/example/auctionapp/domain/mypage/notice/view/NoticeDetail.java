package com.example.auctionapp.domain.mypage.notice.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.auctionapp.databinding.ActivityNoticeDetailBinding;
import com.example.auctionapp.domain.mypage.constant.MypageConstants;
import com.example.auctionapp.domain.mypage.notice.constant.NoticeConstants;
import com.example.auctionapp.domain.mypage.notice.dto.NoticeListResponse;
import com.example.auctionapp.domain.mypage.notice.dto.NoticeResponse;
import com.example.auctionapp.domain.mypage.notice.model.NoticeData;
import com.example.auctionapp.domain.mypage.notice.presenter.NoticePresenter;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class NoticeDetail extends AppCompatActivity {
    private ActivityNoticeDetailBinding binding;

//    String temp = "안녕하세요, HUP!입니다.\n" +
//            "\n" +
//            "더욱 발전된 서비스 제공을 위해 어플리케이션 점검이 진행될 예정입니다. 서비스 점검 시간에는 어플 사용이 잠시 중단됩니다. 사용자 여러분들의 양해를 부탁드립니다.\n" +
//            "해당시간 내에 문의 사항이 있으실 경우 HUP! 고객센터 1600-9494로 전화 부탁드립니다.\n" +
//            "\n" +
//            "점검일시\n" +
//            "2021년 11월 26일(금) 오전 3시 - 5시\n" +
//            "업데이트 시간은 상황에 따라 단축 또는 연장될 수 있습니다. ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoticeDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Intent intent = getIntent();
        Long noticeId = intent.getLongExtra("noticeId", 0);

        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getNotice(noticeId)
                .enqueue(MainRetrofitTool.getCallback(new getNoticeDetailCallback()));

        binding.goback.bringToFront();
        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    public class getNoticeDetailCallback implements MainRetrofitCallback<NoticeResponse> {
        @Override
        public void onSuccessResponse(Response<NoticeResponse> response) {
            String title = response.body().getTitle();
            String body = response.body().getBody();
            String userName = response.body().getUserName();
            binding.noticeTitle.setText(title);
            binding.noticeDate.setText(userName);
            binding.noticeContent.setText(body);
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
}
