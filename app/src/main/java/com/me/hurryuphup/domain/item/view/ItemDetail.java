package com.me.hurryuphup.domain.item.view;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.me.hurryuphup.databinding.ActivityItemDetailBinding;
import com.me.hurryuphup.domain.item.model.qnaData;
import com.me.hurryuphup.domain.item.presenter.ItemDetailPresenter;
import com.me.hurryuphup.domain.pricesuggestion.view.BidPage;
import com.me.hurryuphup.domain.user.constant.Constants;

public class ItemDetail extends AppCompatActivity implements ItemDetailView {
    private ActivityItemDetailBinding binding;
    private ItemDetailPresenter presenter;

    private Long itemId;

    @Override
    public void onResume() {
        presenter = new ItemDetailPresenter(this, binding, getApplicationContext());
        presenter.initializeImageData();
        presenter.getItemInfoCallback(itemId);  //item info

        super.onResume();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityItemDetailBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new ItemDetailPresenter(this, binding, getApplicationContext());

        Intent intent = getIntent();
        this.itemId = intent.getLongExtra("itemId",0);

        if(Constants.userId == null) {
            binding.participateButton.setText("로그인 후 이용해주세요.");
            binding.participateButton.setEnabled(false);
            binding.participateButton.setBackgroundColor(Color.GRAY);
            binding.isheart.setVisibility(View.GONE);
        }
        binding.participateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BidPage.class);
                intent.putExtra("itemId", itemId);
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
        presenter.init(itemId);
        presenter.getItemInfoCallback(itemId);  //item info

        //item delete
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.deleteItem(itemId);
            }
        });

        //QNA dumidata
//        presenter.qnaInit();
        // Q&A 준비중
        binding.preparing.setVisibility(View.VISIBLE);
        binding.qaCount.setVisibility(View.GONE);
        binding.itemDetailQnaList.setVisibility(View.GONE);

        //view all qnas
        binding.viewAll.setVisibility(View.INVISIBLE);
//        binding.viewAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent t = new Intent(getApplicationContext(), QnA.class);
//                t.putExtra("itemId", itemId);
//                startActivity(t);
//            }
//        });

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
    }

    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}


