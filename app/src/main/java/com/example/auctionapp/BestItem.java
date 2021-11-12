package com.example.auctionapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class BestItem {
    int btImage; 
    String btName;
    String btTime;
    int btTempMax;

    public BestItem(int btImage, String btName, String btTime, int btPrice) {
        this.btImage = btImage;
        this.btName = btName;
        this.btTime = btTime;
        this.btTempMax = btPrice;
    }

    public String getBtName() {
        return btName;
    }
    public void setBtName(String btName) {
        this.btName = btName;
    }
    public String getBtTime() {
        return btTime;
    }
    public void setBtTime(String btTime) {
        this.btTime = btTime;
    }
    public int getBtTempMax() {
        return btTempMax;
    }
    public void setBtTempMax(int btPrice) {
        this.btTempMax = btPrice;
    }
    public int getBtImage() {
        return btImage;
    }
    public void setBtImage(int btImage) {
        this.btImage = btImage;
    }

}

class BestItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // adapter에 들어갈 list
    private ArrayList<BestItem> BestItemData = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_best_item, parent, false);
        return new BestItemAdapter.BestItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((BestItemAdapter.BestItemViewHolder) holder).onBind(BestItemData.get(position));
    }

    @Override
    public int getItemCount() {
        return BestItemData.size();
    }

    void addItem(BestItem data) {
        // 외부에서 item을 추가시킬 함수입니다.
        BestItemData.add(data);
    }

    public interface OnItemClickListener {
        void onItemClick(View v, int position) ;
    }
    // 리스너 객체 참조를 저장하는 변수
    private BestItemAdapter.OnItemClickListener mListener = null ;

    public void setOnItemClickListener(BestItemAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    public class BestItemViewHolder extends RecyclerView.ViewHolder {

        ImageView bt_image;
        TextView bt_item_name;
        TextView bt_purchase_date;
        TextView bt_temp_max;


        public BestItemViewHolder(@NonNull View itemView) {
            super(itemView);

            bt_image = itemView.findViewById(R.id.bt_image);
            bt_item_name = itemView.findViewById(R.id.bt_item_name);
            bt_purchase_date = itemView.findViewById(R.id.bt_purchase_date);
            bt_temp_max = itemView.findViewById(R.id.bt_temp_max);

        }

        public void onBind(BestItem data) {
            bt_image.setImageResource(data.getBtImage());
            bt_item_name.setText(data.getBtName());
            bt_purchase_date.setText(data.getBtTime());
            bt_temp_max.setText(data.getBtTempMax() + "원");
        }
    }
}

