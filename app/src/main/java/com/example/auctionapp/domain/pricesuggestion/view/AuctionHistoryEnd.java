package com.example.auctionapp.domain.pricesuggestion.view;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.pricesuggestion.dto.MaximumPriceResponse;
import com.example.auctionapp.domain.pricesuggestion.dto.PriceSuggestionListResponse;
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

import lombok.Getter;
import lombok.Setter;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class AuctionHistoryEnd extends Fragment {

    ViewGroup viewGroup;
    AuctionHistoryEndAdapter adapter;
    AuctionHistoryEndData data;
    List<AuctionHistoryEndData> auctionHistoryEndDataList = new ArrayList<>();
    int maximumPriceCount;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_auction_history_end, container, false);

        init();
        getData();

        return viewGroup;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void init(){
        RecyclerView recyclerView = viewGroup.findViewById(R.id.auctionHistoryEndRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new AuctionHistoryEndAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new AuctionHistoryEndAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
//                Intent intent = new Intent(getContext(), ItemDetail.class);
//                startActivity(intent);
            }
        });

        //구분선
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), new LinearLayoutManager(getContext()).getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }
    private void getData(){
        //일단 레이아웃만
//        AuctionHistoryEndData data = new AuctionHistoryEndData(R.drawable.rectangle, "스타벅스 리유저블 컵", 70000, "뽀로로");
//        adapter.addItem(data);
//        data = new AuctionHistoryEndData(R.drawable.rectangle, "스타벅스 리유저블 컵", 70000, "뽀로로");
//        adapter.addItem(data);
        maximumPriceCount = 0;
        auctionHistoryEndDataList = new ArrayList<>();
        RetrofitTool.getAPIWithAuthorizationToken(Constants.token).getAllPriceSuggestionByUserId(Constants.userId)
                .enqueue(MainRetrofitTool.getCallback(new getAllPriceSuggestionByUserIdCallback()));
    }

    private class getAllPriceSuggestionByUserIdCallback implements MainRetrofitCallback<PaginationDto<List<PriceSuggestionListResponse>>> {

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onSuccessResponse(Response<PaginationDto<List<PriceSuggestionListResponse>>> response) {
            for(int i=0; i<response.body().getData().size(); i++){
                if(response.body().getData().get(i).isAcceptState()==true) {
                    LocalDateTime startDateTime = LocalDateTime.now();
                    LocalDateTime endDateTime = response.body().getData().get(i).getAuctionClosingDate();
                    String days = String.valueOf(ChronoUnit.DAYS.between(startDateTime, endDateTime));
                    String hours = String.valueOf(ChronoUnit.HOURS.between(startDateTime, endDateTime));
                    String minutes = String.valueOf(ChronoUnit.MINUTES.between(startDateTime, endDateTime));
                    Long itemId = response.body().getData().get(i).getItemId();
                    String fileNameMajor = response.body().getData().get(i).getFileNames().get(0);
                    String itemName = response.body().getData().get(i).getItemName();
                    int suggestionPrice = response.body().getData().get(i).getSuggestionPrice();
                    String userName = response.body().getData().get(i).getUserName();
                    if (response.body().getData().get(i).getFileNames().size() != 0) {
                        data = new AuctionHistoryEndData(itemId,
                                fileNameMajor,
                                itemName,
                                suggestionPrice,
                                userName);
                    } else {
                        data = new AuctionHistoryEndData(itemId,
                                fileNameMajor,
                                itemName,
                                suggestionPrice,
                                userName);
                    }
                    auctionHistoryEndDataList.add(data);
                }
//                setAnimation();
            }
            Log.d(TAG, "retrofit success, idToken: " + response.body().toString());

        }
        @Override
        public void onFailResponse(Response<PaginationDto<List<PriceSuggestionListResponse>>> response) throws IOException, JSONException {
            System.out.println("errorBody"+response.errorBody().string());
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }

}
@Getter
@Setter
class AuctionHistoryEndData {
    Long itemId;
    String imageURL;       //나중에 수정 (int -> string url)
    String itemName;
    int itemPrice;
    String sellerName;

    public AuctionHistoryEndData(Long itemId, String imageURL, String itemName, int itemPrice, String sellerName){
        this.itemId = itemId;
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.sellerName = sellerName;
    }

}

class AuctionHistoryEndAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // adapter에 들어갈 list 입니다.
    private ArrayList<AuctionHistoryEndData> listData = new ArrayList<>();
    Context context;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_auction_history_end_recyclerview, parent, false);
        return new AuctionHistoryEndViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AuctionHistoryEndViewHolder)holder).onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    void addItem(AuctionHistoryEndData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mListener = null ;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {

        this.mListener = listener ;
    }

    public class AuctionHistoryEndViewHolder extends  RecyclerView.ViewHolder {

        ImageView auc_history_end_img;
        TextView auc_history_end_edt_name;
        TextView auc_history_end_edt_myPrice;
        TextView auc_history_end_edt_seller;

        public AuctionHistoryEndViewHolder(@NonNull View itemView) {
            super(itemView);

            auc_history_end_img = itemView.findViewById(R.id.auc_history_end_img);
            auc_history_end_edt_name = itemView.findViewById(R.id.auc_history_end_edt_name);
            auc_history_end_edt_myPrice = itemView.findViewById(R.id.auc_history_end_edt_myPrice);
            auc_history_end_edt_seller = itemView.findViewById(R.id.auc_history_end_edt_seller);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos) ;
                        }
                    }
                }
            });

        }

        public void onBind(AuctionHistoryEndData data){
            Glide.with(context).load(Constants.imageBaseUrl+data.getImageURL()).override(auc_history_end_img.getWidth()
                    ,auc_history_end_img.getHeight()).into(auc_history_end_img);
            auc_history_end_img.setClipToOutline(true);  //item 테두리
            auc_history_end_edt_name.setText(data.getItemName());
            auc_history_end_edt_myPrice.setText(data.getItemPrice()+"");
            auc_history_end_edt_seller.setText(data.getSellerName());
        }
    }
}

