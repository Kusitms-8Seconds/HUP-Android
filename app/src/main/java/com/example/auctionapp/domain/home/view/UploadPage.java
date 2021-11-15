package com.example.auctionapp.domain.home.view;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.file.view.MultiImageAdapter;
import com.example.auctionapp.domain.item.view.SelectCategory;

import java.util.ArrayList;
import java.util.Calendar;

public class UploadPage extends AppCompatActivity {

    // DatePickerDialog
    TextView editDate;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener datePickerEndDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editDate.setText(String.format("%d-%d-%d", year, month+1, dayOfMonth));
        }
    };
    TextView buyDate;
    DatePickerDialog.OnDateSetListener datePickerBuyDate = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            buyDate.setText(String.format("%d-%d-%d", year, month+1, dayOfMonth));
        }
    };

    private RatingBar ratingbar;
    private static final String TAG = "UploadPage";
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    RecyclerView selectedImageRecyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    TextView selectedImageCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);

        EditText itemName = (EditText) findViewById(R.id.editItemName);
        EditText itemPrice = (EditText)findViewById(R.id.editPrice);
        EditText itemContent = (EditText) findViewById(R.id.editItemContent);

        ImageView goBack = (ImageView) findViewById(R.id.goback);
        goBack.bringToFront();
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tt = new Intent(UploadPage.this, MainActivity.class);
                tt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tt);
            }
        });

        // 완료 버튼
        TextView uploadButton = (TextView) findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: upload method
//                String name = itemName.getText().toString();
//                String price = itemPrice.getText().toString();
//                String content = itemContent.getText().toString();
            }
        });

        // 구매 일자
        buyDate = findViewById(R.id.editAuctionBuyDate);
        Calendar calender = Calendar.getInstance();
        buyDate.setText(calender.get(Calendar.YEAR) +"-"+ (calender.get(Calendar.MONTH)+1) +"-"+ calender.get(Calendar.DATE));
        buyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(UploadPage.this, datePickerBuyDate,
                        calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DATE));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });


        // 경매 종료 일자
        editDate = findViewById(R.id.editAuctionFinalDate);
        Calendar cal = Calendar.getInstance();
        editDate.setText(cal.get(Calendar.YEAR) +"-"+ (cal.get(Calendar.MONTH)+1) +"-"+ cal.get(Calendar.DATE));
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(UploadPage.this, datePickerEndDate,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        // 물건 상태
        ratingbar = findViewById(R.id.itemStateRatingBar);
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getApplicationContext(),"New Rating: "+ rating, Toast.LENGTH_SHORT).show();

            }
        });

        // 이미지 업로드
        selectedImageRecyclerView = (RecyclerView) findViewById(R.id.selectedImageRecyclerView);
        selectedImageCount = (TextView) findViewById(R.id.selectedImageCount);
        ImageView chooseImage = (ImageView) findViewById(R.id.chooseImage);
        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 앨범으로 이동
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); //다중 이미지를 가져올 수 있도록
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);
            }
        });

        // category
        LinearLayout selectCategory = (LinearLayout) findViewById(R.id.selectCategoryLayout);
        selectCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tt = new Intent(UploadPage.this, SelectCategory.class);
                startActivity(tt);
            }
        });
        TextView itemCategory = (TextView) findViewById(R.id.selectItemCategory);
        Intent getCategoryIntent = getIntent();
        String itemCT = getCategoryIntent.getStringExtra("itemCategory");
        itemCategory.setText(itemCT);

    }

    // select image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        uriList.clear();    // 이미지 다시 선택 시 초기화

        if(requestCode == 2222){
            if(data == null){   // 어떤 이미지도 선택하지 않은 경우
                Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
            }
            else{   // 이미지를 하나라도 선택한 경우
                if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                    Log.e("single choice: ", String.valueOf(data.getData()));
                    selectedImageCount.setText("1/10");
                    Uri imageUri = data.getData();
                    uriList.add(imageUri);

                    adapter = new MultiImageAdapter(uriList, getApplicationContext());
                    selectedImageRecyclerView.setAdapter(adapter);
                    selectedImageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
                }
                else{      // 이미지를 여러장 선택한 경우
                    ClipData clipData = data.getClipData();
                    Log.e("clipData", String.valueOf(clipData.getItemCount()));
                    selectedImageCount.setText(String.valueOf(clipData.getItemCount()) + "/10");

                    if(clipData.getItemCount() > 10){   // 선택한 이미지가 11장 이상인 경우
                        Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                    }
                    else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                        Log.e(TAG, "multiple choice");

                        for (int i = 0; i < clipData.getItemCount(); i++){
                            Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.
                            try {
                                uriList.add(imageUri);  //uri를 list에 담는다.
                            } catch (Exception e) {
                                Log.e(TAG, "File select error", e);
                            }
                        }
                        adapter = new MultiImageAdapter(uriList, getApplicationContext());
                        selectedImageRecyclerView.setAdapter(adapter);   // 리사이클러뷰에 어댑터 세팅
                        selectedImageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));     // 리사이클러뷰 수평 스크롤 적용
                    }
                }
            }
        }
    }
}


