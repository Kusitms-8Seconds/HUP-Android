package com.example.auctionapp.domain.item.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auctionapp.R;

public class ItemList extends Fragment {

    ViewGroup viewGroup;

    ItemDataAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_itemlist, container, false);

        init();
        getData();

        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    private void init(){
        RecyclerView recyclerView = viewGroup.findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new ItemDataAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ItemDataAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Intent intent = new Intent(getContext(), ItemDetail.class);
                startActivity(intent);
            }
        });

        //구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        ImageView searchView = (ImageView) viewGroup.findViewById(R.id.searchView);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Search.class);
                startActivity(intent);
            }
        });

    }

    private void getData(){
        //일단 레이아웃만
        ItemData data = new ItemData(R.drawable.rectangle, "아이폰 11 프로 256GB", 530000, "14:46", 30, 4);
        adapter.addItem(data);
        data = new ItemData(R.drawable.rectangle, "아이폰 11 프로 64GB", 500000, "82:33", 32, 5);
        adapter.addItem(data);
        data = new ItemData(R.drawable.rectangle, "아이폰 11 미니 A급 256GB", 420000, "34:07", 23, 1);
        adapter.addItem(data);
        data = new ItemData(R.drawable.rectangle, "아이폰 11 미니 256GB 레드 미개봉 중고", 552000, "34:04", 27, 2);
        adapter.addItem(data);

    }

}