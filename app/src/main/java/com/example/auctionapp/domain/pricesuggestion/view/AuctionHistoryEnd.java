package com.example.auctionapp.domain.pricesuggestion.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class AuctionHistoryEnd extends Fragment {

    ViewGroup viewGroup;
    AuctionHistoryEndAdapter adapter;

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
        AuctionHistoryEndData data = new AuctionHistoryEndData(R.drawable.rectangle, "스타벅스 리유저블 컵", 70000, "뽀로로");
        adapter.addItem(data);
        data = new AuctionHistoryEndData(R.drawable.rectangle, "스타벅스 리유저블 컵", 70000, "뽀로로");
        adapter.addItem(data);
    }

}
class AuctionHistoryEndData {
    int imageURL;       //나중에 수정 (int -> string url)
    String itemName;
    int itemPrice;
    String sellerName;

    public AuctionHistoryEndData(int imageURL, String itemName, int itemPrice, String sellerName){
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

class AuctionHistoryEndAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    // adapter에 들어갈 list 입니다.
    private ArrayList<AuctionHistoryEndData> listData = new ArrayList<>();

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
            auc_history_end_img.setImageResource(data.getImage());
            auc_history_end_img.setClipToOutline(true);  //item 테두리
            auc_history_end_edt_name.setText(data.getItemName());
            auc_history_end_edt_myPrice.setText(data.getItemPrice()+"");
            auc_history_end_edt_seller.setText(data.getSellerName());
        }
    }
}

