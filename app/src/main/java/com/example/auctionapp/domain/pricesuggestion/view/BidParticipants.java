package com.example.auctionapp.domain.pricesuggestion.view;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auctionapp.R;

import java.util.ArrayList;

public class BidParticipants {
    int ptImage;    //나중에 url로 수정
    String ptName;
    int ptPrice;
    String ptTime;

    public BidParticipants(int ptImage, String ptName, int ptPrice, String ptTime) {
        this.ptImage = ptImage;
        this.ptName = ptName;
        this.ptPrice = ptPrice;
        this.ptTime = ptTime;
    }

    public String getPtName() {
        return ptName;
    }
    public void setPtName(String ptName) {
        this.ptName = ptName;
    }
    public String getPtTime() {
        return ptTime;
    }
    public void setPtTime(String ptTime) {
        this.ptTime = ptTime;
    }
    public int getPtPrice() {
        return ptPrice;
    }
    public void setPtPrice(int ptPrice) {
        this.ptPrice = ptPrice;
    }
    public int getPtImage() {
        return ptImage;
    }
    public void setPtImage(int ptImage) {
        this.ptImage = ptImage;
    }

}

//class PTAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
//
//    // adapter에 들어갈 list 입니다.
//    private ArrayList<BidParticipants> listData = new ArrayList<>();
//
//    @NonNull
//    @Override
//    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_participants_list, parent, false);
//        return new PTViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
//        ((PTViewHolder) holder).onBind(listData.get(position));
//    }
//
//    @Override
//    public int getItemCount() {
//        return listData.size();
//    }
//
//    void addItem(BidParticipants data) {
//        // 외부에서 item을 추가시킬 함수입니다.
//        listData.add(data);
//    }
//
//    public interface OnItemClickListener {
//        void onItemClick(View v, int position);
//    }
//
//    public class PTViewHolder extends RecyclerView.ViewHolder {
//
//        ImageView pt_image;
//        TextView pt_name;
//        TextView pt_price;
//
//
//        public PTViewHolder(@NonNull View itemView) {
//            super(itemView);
//
//            pt_image = itemView.findViewById(R.id.participants_image);
//            pt_name = itemView.findViewById(R.id.participants_name);
//            pt_price = itemView.findViewById(R.id.participants_price);
//
//        }
//
//        public void onBind(BidParticipants data) {
//            pt_image.setImageResource(data.getPtImage());
//            pt_name.setText(data.getPtName());
//            pt_price.setText(data.getPtPrice() + "원");
//        }
//    }
//}
