package com.example.auctionapp.domain.item.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.item.model.SellHistoryEndData;
import com.example.auctionapp.domain.pricesuggestion.constant.PriceConstants;
import com.example.auctionapp.domain.pricesuggestion.presenter.BidPagePresenter;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.domain.user.dto.UserInfoResponse;
import com.example.auctionapp.global.firebase.FCMRequest;
import com.example.auctionapp.global.firebase.FCMResponse;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import lombok.Getter;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

@Getter
public class SellHistoryEndAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // adapter에 들어갈 list 입니다.
    private ArrayList<SellHistoryEndData> listData = new ArrayList<>();
    Context context;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_sell_history_end_recyclerview, parent, false);
        return new SellHistoryEndViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((SellHistoryEndViewHolder)holder).onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void addItem(SellHistoryEndData data) {
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

    public class SellHistoryEndViewHolder extends  RecyclerView.ViewHolder {

        ImageView sell_history_end_img;
        TextView sell_history_end_edt_name;
        TextView sell_history_end_edt_myPrice;
        TextView sell_history_end_edt_seller;

        public SellHistoryEndViewHolder(@NonNull View itemView) {
            super(itemView);

            sell_history_end_img = itemView.findViewById(R.id.sell_history_end_img);
            sell_history_end_edt_name = itemView.findViewById(R.id.sell_history_end_edt_name);
            sell_history_end_edt_myPrice = itemView.findViewById(R.id.sell_history_end_edt_myPrice);
            sell_history_end_edt_seller = itemView.findViewById(R.id.sell_history_end_edt_seller);

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
//            itemView.findViewById(R.id.btn_bid).setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    int pos = getAdapterPosition() ;
//                    if (pos != RecyclerView.NO_POSITION) {
//                        // 리스너 객체의 메서드 호출.
////                        if (mListener != null) {
////                            mListener.onItemClick(v, pos);
////                        }
//                        FCMRequest fcmRequest = FCMRequest.of("body", Constants.targetToken, "title");
//                        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).pushMessage(fcmRequest)
//                                .enqueue(MainRetrofitTool.getCallback(new pushMessageCallback()));
//                    }
//                }
//            });

        }

        public void onBind(SellHistoryEndData data){
            Glide.with(context).load(Constants.imageBaseUrl+data.getImageURL()).override(sell_history_end_img.getWidth()
                    ,sell_history_end_img.getHeight()).into(sell_history_end_img);
            sell_history_end_img.setClipToOutline(true);  //item 테두리
            sell_history_end_edt_name.setText(data.getItemName());
            sell_history_end_edt_myPrice.setText(data.getItemPrice()+"");
            sell_history_end_edt_seller.setText(data.getBidderName());
        }


    }
}