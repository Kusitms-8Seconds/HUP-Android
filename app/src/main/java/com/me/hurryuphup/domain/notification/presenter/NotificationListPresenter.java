package com.me.hurryuphup.domain.notification.presenter;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.me.hurryuphup.databinding.ActivityNotificationListBinding;
import com.me.hurryuphup.domain.mypage.constant.MypageConstants;
import com.me.hurryuphup.domain.notification.adapter.NotificationAdapter;
import com.me.hurryuphup.domain.notification.dto.NotificationListResponse;
import com.me.hurryuphup.domain.notification.model.NotificationListData;
import com.me.hurryuphup.domain.notification.view.NotificationListView;
import com.me.hurryuphup.domain.user.constant.Constants;
import com.me.hurryuphup.global.dto.PaginationDto;
import com.me.hurryuphup.global.retrofit.MainRetrofitCallback;
import com.me.hurryuphup.global.retrofit.MainRetrofitTool;
import com.me.hurryuphup.global.retrofit.RetrofitTool;
import com.me.hurryuphup.global.util.ErrorMessageParser;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class NotificationListPresenter implements Presenter {
    // Attributes
    private NotificationListView notificationListView;
    private ActivityNotificationListBinding binding;
    private Context context;

    ArrayList<NotificationListData> notificationList = new ArrayList<NotificationListData>();
    NotificationAdapter notificationAdapter;
    int page;
    int size = 10;

    // Constructor
    public NotificationListPresenter(NotificationListView notificationListView, ActivityNotificationListBinding binding, Context context){
        this.notificationListView = notificationListView;
        this.binding = binding;
        this.context = context;
    }

    @Override
    public void init(Long userId) {
        notificationAdapter = new NotificationAdapter(context, notificationList);
        binding.notificationListView.setAdapter(notificationAdapter);
        binding.moreButton.setVisibility(View.GONE);
        binding.progressbar.setVisibility(View.GONE);

        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getNotificationList(userId, page, size)
                .enqueue(MainRetrofitTool.getCallback(new getAllNotificationListCallback()));

        binding.moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.progressbar.setVisibility(View.VISIBLE);
                page++;
                RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getNotificationList(userId, page, size)
                        .enqueue(MainRetrofitTool.getCallback(new getAllNotificationListCallback()));
                binding.progressbar.setVisibility(View.GONE);
            }
        });
    }

    public class getAllNotificationListCallback implements MainRetrofitCallback<PaginationDto<List<NotificationListResponse>>> {
        @Override
        public void onSuccessResponse(Response<PaginationDto<List<NotificationListResponse>>> response) {
            if(response.body().getCurrentElements() >= 10) binding.moreButton.setVisibility(View.VISIBLE);
            for(int i=0; i<response.body().getData().size(); i++){
                String message = response.body().getData().get(i).getMessage();

                String createdTime = "";
                LocalDateTime endDateTime = LocalDateTime.now();
                LocalDateTime startDateTime = response.body().getData().get(i).getCreatedDate();
                String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
                String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
                String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime)%60);
                if(Integer.parseInt(hours) >= 24) {
                    hours = String.valueOf(Integer.parseInt(hours)%24);
                    createdTime = days + "일 " + hours + "시간 " + minutes + "분 전";
                } else if(Integer.parseInt(hours) > 0)
                    createdTime = hours + "시간 " + minutes + "분 전";
                else if(Integer.parseInt(hours) <= 0)
                    createdTime = minutes + "분 전";

                notificationList.add(new NotificationListData(message, createdTime));
                notificationAdapter.notifyDataSetChanged();
            }
            Log.d(TAG, MypageConstants.EMyPageCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<NotificationListResponse>>> response) throws IOException, JSONException {
            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string(), context);
            notificationListView.showToast(errorMessageParser.getParsedErrorMessage());
            Log.d(TAG, MypageConstants.EMyPageCallback.rtFailResponse.getText() + ":"+response.errorBody().string());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e( MypageConstants.EMyPageCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
}
