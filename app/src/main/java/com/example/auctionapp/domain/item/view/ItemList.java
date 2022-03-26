package com.example.auctionapp.domain.item.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.auctionapp.databinding.ActivityItemlistBinding;
import com.example.auctionapp.domain.item.adapter.ItemDataAdapter;
import com.example.auctionapp.domain.item.constant.ItemConstants;
import com.example.auctionapp.domain.item.dto.ItemDetailsResponse;
import com.example.auctionapp.domain.item.model.ItemData;
import com.example.auctionapp.domain.item.presenter.ItemListPresenter;
import com.example.auctionapp.domain.pricesuggestion.dto.ParticipantsResponse;
import com.example.auctionapp.domain.scrap.dto.ScrapCountResponse;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.dto.PaginationDto;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class ItemList extends Fragment implements ItemListView {
    private ActivityItemlistBinding binding;
    ItemListPresenter presenter;

    @Override
    public void onResume() {
        presenter = new ItemListPresenter(this, binding, getActivity());
        presenter.init();
        presenter.getData();

        super.onResume();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        binding = ActivityItemlistBinding.inflate(inflater, container, false);
        View view = binding.getRoot();

        presenter = new ItemListPresenter(this, binding, getActivity());

        presenter.init();
        presenter.getData();

        binding.swipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /* swipe 시 진행할 동작 */
                presenter.init();
                presenter.getData();
                /* 업데이트가 끝났음을 알림 */
                binding.swipe.setRefreshing(false);
            }
        });

        binding.searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), Search.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void showToast(String parsedErrorMessage) {
        Toast.makeText(getActivity(), parsedErrorMessage, Toast.LENGTH_SHORT).show();
    }
}
