package com.example.auctionapp.domain.item.view;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityMakeQnaBinding;
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
    private ActivityMakeQnaBinding binding;

    private Long EndItemId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMakeQnaBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        ImageView goBack = (ImageView) findViewById(R.id.goback);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.chattingItemImage.setClipToOutline(true);
        Intent intent = getIntent();
        EndItemId = intent.getLongExtra("itemId", 0);
        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).getItem(Long.valueOf(7))
                .enqueue(MainRetrofitTool.getCallback(new EditQnA.getItemDetailsCallback()));
    }
    public class getItemDetailsCallback implements MainRetrofitCallback<ItemDetailsResponse> {
        @Override
        public void onSuccessResponse(Response<ItemDetailsResponse> response) {
            binding.chattingItemDetailName.setText(response.body().getItemName());
            if(response.body().getFileNames().size()!=0){
                String fileThumbNail = response.body().getFileNames().get(0);
                Glide.with(getApplicationContext()).load(Constants.imageBaseUrl+fileThumbNail).into(binding.chattingItemImage);
            }
            binding.chattingItemDetailCategory.setText(response.body().getCategory().getName());
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ItemDetailsResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            binding.chattingItemDetailName.setText("?연결실패?");
            Log.e("연결실패", t.getMessage());
        }
    }
}
