package com.example.auctionapp.domain.mypage.notice.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.auctionapp.databinding.ActivityNoticeBinding;
import com.example.auctionapp.domain.mypage.constant.MypageConstants;
import com.example.auctionapp.domain.mypage.notice.adapter.NoticeAdapter;
import com.example.auctionapp.domain.mypage.notice.constant.NoticeConstants;
import com.example.auctionapp.domain.mypage.notice.dto.NoticeListResponse;
import com.example.auctionapp.domain.mypage.notice.model.NoticeData;
import com.example.auctionapp.domain.mypage.notice.view.NoticeDetail;
import com.example.auctionapp.domain.mypage.notice.view.NoticeView;
import com.example.auctionapp.domain.mypage.presenter.MypagePresenter;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserInfoResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.util.ErrorMessageParser;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class NoticePresenter implements NoticePresenterInterface {
    // Attributes
    private NoticeView noticeView;
    private ActivityNoticeBinding binding;
    private Context context;

    ArrayList<NoticeData> noticeList = new ArrayList<NoticeData>();
    NoticeAdapter noticeAdapter;

    // Constructor
    public NoticePresenter(NoticeView noticeView, ActivityNoticeBinding binding, Context context){
        this.noticeView = noticeView;
        this.binding = binding;
        this.context = context;
    }

    @Override
    public void init() {
        noticeAdapter = new NoticeAdapter(context, noticeList);
        binding.noticeListView.setAdapter(noticeAdapter);

        binding.noticeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String noticeTitle = noticeList.get(position).getNoticeTitle();
                String noticeDate = noticeList.get(position).getNoticeDate();
                Intent intent = new Intent(context, NoticeDetail.class);
                intent.putExtra(NoticeConstants.ENoticeDetails.noticeTitle.getText(), noticeTitle);
                intent.putExtra(NoticeConstants.ENoticeDetails.noticeDate.getText(), noticeDate);
                context.startActivity(intent);
            }
        });

        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getAllNotice()
                .enqueue(MainRetrofitTool.getCallback(new getAllNoticeCallback()));

    }

    public class getAllNoticeCallback implements MainRetrofitCallback<NoticeListResponse> {
        @Override
        public void onSuccessResponse(Response<NoticeListResponse> response) {
            String title = response.body().getTitle();
            String userName = response.body().getUserName();
            noticeList.add(new NoticeData(title, userName));
            noticeAdapter.notifyDataSetChanged();
            Log.d(TAG, MypageConstants.EMyPageCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<NoticeListResponse> response) throws IOException, JSONException {
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
