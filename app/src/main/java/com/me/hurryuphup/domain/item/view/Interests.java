package com.me.hurryuphup.domain.item.view;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;

import com.me.hurryuphup.R;
import com.me.hurryuphup.databinding.ActivityInterestsBinding;
import com.me.hurryuphup.domain.item.adapter.CategoryAdapter;
import com.me.hurryuphup.domain.item.model.Category;

import java.util.ArrayList;

public class Interests extends AppCompatActivity {
    private ActivityInterestsBinding binding;
    CategoryAdapter adapter;
    ArrayList<String> interestArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInterestsBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        interestArrayList = new ArrayList<>();
        // 뒤로가기 화살표
        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        // 완료txt
        binding.completeB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                onBackPressed();
            }
        });

        adapter = new CategoryAdapter();
        binding.interestCT.setAdapter(adapter);
        init();

        // 이벤트 처리 리스너 설정
        binding.interestCT.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Category item = (Category) adapter.getItem(position);
                if(item.getSelected()) {
                    item.setSelected(false);
                    interestArrayList.remove(item.getCategory());
                }else {
                    item.setSelected(true);
                    interestArrayList.add(item.getCategory());
                }
                adapter.notifyDataSetChanged();
//                Toast.makeText(getApplicationContext(), "선택 :"+item.getCategory(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    public void init() {
        adapter.addItem(new Category(false, R.drawable.necktie, "남성 패션/잡화"));
        adapter.addItem(new Category(false, R.drawable.womans_clothes, "여성 패션"));
        adapter.addItem(new Category(false, R.drawable.lipstick, "뷰티/미용"));
        adapter.addItem(new Category(false, R.drawable.game, "게임/취미"));
        adapter.addItem(new Category(false, R.drawable.bowling, "스포츠/레저"));
        adapter.addItem(new Category(false, R.drawable.television, "생활가전"));
        adapter.addItem(new Category(false, R.drawable.desktop, "디지털기기"));
        adapter.addItem(new Category(false, R.drawable.dog, "반려동물 용품"));
        adapter.addItem(new Category(false, R.drawable.book, "도서/음반"));
        adapter.addItem(new Category(false, R.drawable.plant, "식물"));
        adapter.addItem(new Category(false, R.drawable.baby, "유아용품"));
        adapter.addItem(new Category(false, R.drawable.bed, "가구"));

    }
}
