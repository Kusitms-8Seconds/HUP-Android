package com.example.auctionapp.domain.pricesuggestion.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.user.constant.Constants;

import java.util.ArrayList;

public class AuctionNowAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // adapter에 들어갈 list 입니다.
    private ArrayList<AuctionNow> AuctionNowData = new ArrayList<>();
    Context context;

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        context = recyclerView.getContext();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_home_auction_list, parent, false);
        return new AuctionNowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((AuctionNowViewHolder) holder).onBind(AuctionNowData.get(position));
    }

    @Override
    public int getItemCount() {
        return AuctionNowData.size();
    }

    public void addItem(AuctionNow data) {
        // 외부에서 item을 추가시킬 함수입니다.
        AuctionNowData.add(data);
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }

    // 리스너 객체 참조를 저장하는 변수
    private OnItemClickListener mAuctionListener = null;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mAuctionListener = listener;
    }

    public class AuctionNowViewHolder extends RecyclerView.ViewHolder {

        ImageView item_image;
        TextView item_name;
        TextView item_upPrice;
        TextView item_date;
        TextView item_info;

        public AuctionNowViewHolder(@NonNull View itemView) {
            super(itemView);

            item_image = itemView.findViewById(R.id.sell_history_ongoing_img);
            item_name = itemView.findViewById(R.id.sell_history_ongoing_edt_name);
            item_upPrice = itemView.findViewById(R.id.auc_list_price);
            item_date = itemView.findViewById(R.id.sell_history_ongoing_max);
            item_info = itemView.findViewById(R.id.sell_history_ongoing_myPrice);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mAuctionListener != null) {
                            mAuctionListener.onItemClick(v, pos);
                        }
                    }
                }
            });

        }

        public void onBind(AuctionNow data) {
            Glide.with(context).load(Constants.imageBaseUrl+data.getImageURL()).override(item_image.getWidth()
                    ,item_image.getHeight()).into(item_image);
            item_image.setClipToOutline(true);  //item 테두리
            item_name.setText(data.getItemName());
            item_upPrice.setText(data.getItemPrice() + "");
            item_date.setText(data.getDate());
            item_info.setText(data.getItemInfo() + "");
        }
    }
}
