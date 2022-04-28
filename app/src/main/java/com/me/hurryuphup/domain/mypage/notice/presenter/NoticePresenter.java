package com.me.hurryuphup.domain.mypage.notice.presenter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.me.hurryuphup.databinding.ActivityNoticeBinding;
import com.me.hurryuphup.domain.mypage.constant.MypageConstants;
import com.me.hurryuphup.domain.mypage.notice.adapter.NoticeAdapter;
import com.me.hurryuphup.domain.mypage.notice.dto.NoticeListResponse;
import com.me.hurryuphup.domain.mypage.notice.model.NoticeData;
import com.me.hurryuphup.domain.mypage.notice.view.NoticeDetail;
import com.me.hurryuphup.domain.mypage.notice.view.NoticeView;
import com.me.hurryuphup.domain.user.constant.Constants;
import com.me.hurryuphup.global.dto.PaginationDto;
import com.me.hurryuphup.global.retrofit.MainRetrofitCallback;
import com.me.hurryuphup.global.retrofit.MainRetrofitTool;
import com.me.hurryuphup.global.retrofit.RetrofitTool;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class NoticePresenter implements NoticePresenterInterface {
    // Attributes
    private NoticeView noticeView;
    private ActivityNoticeBinding binding;
    private Context context;

    ArrayList<NoticeData> noticeList = new ArrayList<NoticeData>();
    NoticeAdapter noticeAdapter;
//    Long noticeId;

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
                Long noticeId = noticeList.get(position).getNoticeId();
                Intent intent = new Intent(context, NoticeDetail.class);
                intent.putExtra("noticeId", noticeId);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });

        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getAllNotice()
                .enqueue(MainRetrofitTool.getCallback(new getAllNoticeCallback()));

    }

    public class getAllNoticeCallback implements MainRetrofitCallback<PaginationDto<List<NoticeListResponse>>> {
        @Override
        public void onSuccessResponse(Response<PaginationDto<List<NoticeListResponse>>> response) {
            for(int i=0; i<response.body().getData().size(); i++){
                Long noticeId = response.body().getData().get(i).getId();
                String title = response.body().getData().get(i).getTitle();
                String userName = response.body().getData().get(i).getUserName();
                noticeList.add(new NoticeData(noticeId, title, userName));
                noticeAdapter.notifyDataSetChanged();
            }
            Log.d(TAG, MypageConstants.EMyPageCallback.rtSuccessResponse.getText() + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<NoticeListResponse>>> response) throws IOException, JSONException {
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
