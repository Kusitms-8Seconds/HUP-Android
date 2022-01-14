package com.example.auctionapp.domain.item.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityItemDetailBinding;
import com.example.auctionapp.domain.item.adapter.ItemDetailViewPagerAdapter;
import com.example.auctionapp.domain.item.adapter.qnaAdapter;
import com.example.auctionapp.domain.item.dto.DefaultResponse;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.model.qnaData;
import com.example.auctionapp.domain.item.presenter.ItemDetailPresenter;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.ParticipantsResponse;
import com.example.auctionapp.domain.pricesuggestion.view.BidPage;
import com.example.auctionapp.domain.scrap.dto.ScrapCheckedRequest;
import com.example.auctionapp.domain.scrap.dto.ScrapCheckedResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapRegisterRequest;
import com.example.auctionapp.domain.scrap.dto.ScrapRegisterResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoRequest;
import com.example.auctionapp.domain.user.dto.UserDetailsInfoResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ItemDetail extends AppCompatActivity implements ItemDetailView {
    private ActivityItemDetailBinding binding;
    private ItemDetailPresenter presenter;

    private Long itemId;
    private Long scrapId;
    public Boolean isHeart = false;

    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new ItemDetailPresenter(this, binding, getApplicationContext());

        Intent intent = getIntent();
//        String getItemId = intent.getExtras().getString("itemId");
        this.itemId = intent.getLongExtra("itemId",0);

        if(Constants.userId == null) {
            binding.participateButton.setText("로그인 후 이용해주세요.");
            binding.participateButton.setEnabled(false);
            binding.participateButton.setBackgroundColor(Color.GRAY);
        }
        binding.participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BidPage.class);
                intent.putExtra("itemId", String.valueOf(itemId));
                startActivity(intent);
            }
        });

        binding.goback.bringToFront();
        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        // viewpager
        presenter.initializeImageData();
        //heart check
        presenter.heartCheckCallback(Constants.userId, itemId);

        //로긘 않 해쓷ㄹ대
        if(Constants.userId==null) {
            binding.isheart.setVisibility(View.GONE);
        }
        binding.isheart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isHeart) {
                    presenter.deleteHeartCallback(scrapId);
                }else {
                    presenter.createHeartCallback(Constants.userId, itemId);

                }
            }
        });

        presenter.getItemInfoCallback(itemId);  //item info
        presenter.getUserInfoCallback(Constants.userId);    //user info

        //item delete
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.deleteItem(itemId);
            }
        });

        //QNA dumidata
        presenter.qnaInit();

        binding.itemDetailQnaList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                qnaData data = (qnaData) parent.getItemAtPosition(position);
                Boolean isFolded = data.getFolded();
                if(isFolded) {
                    data.setFolded(false);
                }else {
                    data.setFolded(true);
                }
            }
        });

        //view all qnas
        binding.viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(getApplicationContext(), QnA.class);
                t.putExtra("itemId", itemId);
                startActivity(t);
            }
        });

    }

}


