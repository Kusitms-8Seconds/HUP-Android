package com.example.auctionapp.domain.item.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auctionapp.R;

import java.util.ArrayList;

public class SellHistoryEnd extends Fragment {
    ViewGroup viewGroup;

    SellHistoryEndAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        viewGroup = (ViewGroup) inflater.inflate(R.layout.activity_sell_history_end, container, false);

        init();
        getData();

        return viewGroup;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    private void init(){
        RecyclerView recyclerView = viewGroup.findViewById(R.id.sellHistoryEndRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new SellHistoryEndAdapter();
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new SellHistoryEndAdapter.OnItemClickListener() {
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
        SellHistoryEndData data = new SellHistoryEndData(R.drawable.rectangle, "닌텐도 스위치", 197000, "소소소소");
        adapter.addItem(data);
        data = new SellHistoryEndData(R.drawable.rectangle, "CGV 영화티켓", 6500, "마블마블");
        adapter.addItem(data);
    }
}
class SellHistoryEndData {
    int imageURL;       //나중에 수정 (int -> string url)
    String itemName;
    int itemPrice;
    String sellerName;

    public SellHistoryEndData(int imageURL, String itemName, int itemPrice, String sellerName){
        this.imageURL = imageURL;
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.sellerName = sellerName;
    }

    public int getImage() {
        return imageURL;
    }
    public void setImage(int imageURL) {
        this.imageURL = imageURL;
    }

    public String getItemName() {
        return itemName;
    }
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getItemPrice() {
        return itemPrice;
    }
    public void setItemPrice(int itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getSellerName() {
        return sellerName;
    }
    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }
}

class SellHistoryEndAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // adapter에 들어갈 list 입니다.
    private ArrayList<SellHistoryEndData> listData = new ArrayList<>();

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

    void addItem(SellHistoryEndData data) {
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

        }

        public void onBind(SellHistoryEndData data){
            sell_history_end_img.setImageResource(data.getImage());
            sell_history_end_img.setClipToOutline(true);  //item 테두리
            sell_history_end_edt_name.setText(data.getItemName());
            sell_history_end_edt_myPrice.setText(data.getItemPrice()+"");
            sell_history_end_edt_seller.setText(data.getSellerName());
        }
    }
}
