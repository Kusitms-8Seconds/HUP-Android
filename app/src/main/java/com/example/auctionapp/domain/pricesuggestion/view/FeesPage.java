package com.example.auctionapp.domain.pricesuggestion.view;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityFeesPageBinding;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.vc.ItemDetail;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;

import java.io.IOException;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class FeesPage extends AppCompatActivity {
    private ActivityFeesPageBinding binding;

    Boolean isFolded;
    int finalPrice;

    //뒤로가기
    @Override
    public void onBackPressed() {
        Intent tt = new Intent(getApplicationContext(), MainActivity.class);    //임시
        tt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(tt);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFeesPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //뒤로가기
        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        Long EndItemId = intent.getLongExtra("itemId", 0);
        Long participantId = intent.getLongExtra("participantId", 0);
        finalPrice = intent.getIntExtra("finalPrice", 0);
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getItem(Long.valueOf(8))
                .enqueue(MainRetrofitTool.getCallback(new FeesPage.getItemDetailsCallback()));
        // 최종 결제 수수료 가격
        int feesPrice = finalPrice * 5 / 100;
        binding.tvFees.setText(feesPrice+"");
        binding.goChatting.setText(feesPrice+"원 결제하기");
        binding.goChatting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tt = new Intent(getApplicationContext(), GoChat.class);
                tt.putExtra("itemId", EndItemId);
                tt.putExtra("participantId", participantId);
                tt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tt);
            }
        });

        //item detail click
        binding.goSeeItemDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ItemDetail.class);
                startActivity(intent);
            }
        });
        // 결제수단 창 접기
        isFolded = false;
        binding.buymethod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFolded) {
                    binding.lyBuymethod.setVisibility(View.VISIBLE);
                    binding.upIcon.setImageResource(R.drawable.fold_up);
                    isFolded = false;
                }else {
                    binding.lyBuymethod.setVisibility(View.GONE);
                    binding.upIcon.setImageResource(R.drawable.down);
                    isFolded = true;
                }
            }
        });
        // 결제수단 선택
        binding.tvTv.setPaintFlags(binding.tvTv.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        binding.methodCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.methodCard.setBackgroundResource(R.drawable.selected_blue);
                binding.methodCard.setTextColor(Color.WHITE);
                binding.methodAccount.setBackgroundResource(R.drawable.box_edge);
                binding.methodAccount.setTextColor(Color.BLACK);
                binding.methodKakaopay.setBackgroundResource(R.drawable.box_edge);
                binding.methodKakaopay.setTextColor(Color.BLACK);
                binding.methodNoAccount.setBackgroundResource(R.drawable.box_edge);
                binding.methodNoAccount.setTextColor(Color.BLACK);
                binding.tvTv.setText(binding.methodCard.getText().toString() + " 안내");
                binding.tvBuyMethod.setText(binding.methodCard.getText().toString());
            }
        });
        binding.methodAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.methodAccount.setBackgroundResource(R.drawable.selected_blue);
                binding.methodAccount.setTextColor(Color.WHITE);
                binding.methodCard.setBackgroundResource(R.drawable.box_edge);
                binding.methodCard.setTextColor(Color.BLACK);
                binding.methodKakaopay.setBackgroundResource(R.drawable.box_edge);
                binding.methodKakaopay.setTextColor(Color.BLACK);
                binding.methodNoAccount.setBackgroundResource(R.drawable.box_edge);
                binding.methodNoAccount.setTextColor(Color.BLACK);
                binding.tvTv.setText(binding.methodAccount.getText().toString() + " 안내");
                binding.tvBuyMethod.setText(binding.methodAccount.getText().toString());
            }
        });
        binding.methodKakaopay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.methodKakaopay.setBackgroundResource(R.drawable.selected_blue);
                binding.methodKakaopay.setTextColor(Color.WHITE);
                binding.methodAccount.setBackgroundResource(R.drawable.box_edge);
                binding.methodAccount.setTextColor(Color.BLACK);
                binding.methodCard.setBackgroundResource(R.drawable.box_edge);
                binding.methodCard.setTextColor(Color.BLACK);
                binding.methodNoAccount.setBackgroundResource(R.drawable.box_edge);
                binding.methodNoAccount.setTextColor(Color.BLACK);
                binding.tvTv.setText(binding.methodKakaopay.getText().toString() + " 안내");
                binding.tvBuyMethod.setText(binding.methodKakaopay.getText().toString());
            }
        });
        binding.methodNoAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.methodNoAccount.setBackgroundResource(R.drawable.selected_blue);
                binding.methodNoAccount.setTextColor(Color.WHITE);
                binding.methodAccount.setBackgroundResource(R.drawable.box_edge);
                binding.methodAccount.setTextColor(Color.BLACK);
                binding.methodKakaopay.setBackgroundResource(R.drawable.box_edge);
                binding.methodKakaopay.setTextColor(Color.BLACK);
                binding.methodCard.setBackgroundResource(R.drawable.box_edge);
                binding.methodCard.setTextColor(Color.BLACK);
                binding.tvTv.setText(binding.methodNoAccount.getText().toString() + " 안내");
                binding.tvBuyMethod.setText(binding.methodNoAccount.getText().toString());
            }
        });

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
            binding.chattingItemDetailPrice.setText(finalPrice+"");    //낙찰가 출력(임시)
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());
        }
        @Override
        public void onFailResponse(Response<ItemDetailsResponse> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            binding.chattingItemDetailPrice.setText("?연결실패?");
            Log.e("연결실패", t.getMessage());
        }
    }
}
