package com.example.auctionapp.domain.item.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.chat.view.ChatRoom;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;

import java.io.IOException;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class EditQnA extends AppCompatActivity {
    private ImageView chattingItemImage;
    private TextView chattingItemDetailName;
    private TextView chattingItemDetailCategory;

    private Long EndItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_qna);

        ImageView goBack = (ImageView) findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        chattingItemImage = (ImageView) findViewById(R.id.chattingItemImage);
        chattingItemDetailName = (TextView) findViewById(R.id.chattingItemDetailName);
        chattingItemDetailCategory = (TextView) findViewById(R.id.chattingItemDetailCategory);
        chattingItemImage.setClipToOutline(true);
        Intent intent = getIntent();
        EndItemId = intent.getLongExtra("itemId", 0);
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getItem(Long.valueOf(7))
                .enqueue(MainRetrofitTool.getCallback(new EditQnA.getItemDetailsCallback()));
    }
    public class getItemDetailsCallback implements MainRetrofitCallback<ItemDetailsResponse> {
        @Override
        public void onSuccessResponse(Response<ItemDetailsResponse> response) {
            chattingItemDetailName.setText(response.body().getItemName());
            if(response.body().getFileNames().size()!=0){
                String fileThumbNail = response.body().getFileNames().get(0);
                Glide.with(getApplicationContext()).load(Constants.imageBaseUrl+fileThumbNail).into(chattingItemImage);
            }
            chattingItemDetailCategory.setText(response.body().getCategory().getName());
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ItemDetailsResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            chattingItemDetailName.setText("?연결실패?");
            Log.e("연결실패", t.getMessage());
        }
    }
}
