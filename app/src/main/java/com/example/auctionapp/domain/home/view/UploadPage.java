package com.example.auctionapp.domain.home.view;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.domain.file.view.MultiImageAdapter;
import com.example.auctionapp.domain.item.constant.ItemConstants.EItemCategory;
import com.example.auctionapp.domain.item.dto.RegisterItemRequest;
import com.example.auctionapp.domain.item.view.SelectCategory;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.domain.item.dto.RegisterItemResponse;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class UploadPage extends AppCompatActivity {

    // DatePickerDialog
    Calendar myCalendar = Calendar.getInstance();
    TextView editBuyDate;
    DatePickerDialog.OnDateSetListener datePickerBuyDate = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            LocalDateTime endDateTime = LocalDateTime.of(year, month+1, dayOfMonth, 00, 00,00,0000);
            String formatDate = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            editBuyDate.setText(formatDate.toString());
        }
    };
    TextView editEndDate;
    DatePickerDialog.OnDateSetListener datePickerEndDate = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            LocalDateTime endDateTime = LocalDateTime.of(year, month+1, dayOfMonth, 00, 00,00,0000);
            String formatDate = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            editEndDate.setText(formatDate.toString());
        }
    };

    private RatingBar ratingbar;
    private static final String TAG = "UploadPage";
    // uri를 담아야하는지 path를 담아야하는지?
    ArrayList<Uri> uriList = new ArrayList<>();     // 이미지의 uri를 담을 ArrayList 객체
    RecyclerView selectedImageRecyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    TextView selectedImageCount;
    int itemStatePoint; //item rating
    HashMap<String,RequestBody> map = new HashMap<String,RequestBody>();    //hashmap 생성
    ArrayList<MultipartBody.Part> files = new ArrayList<>(); // 여러 file들을 담아줄 ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_page);

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

        // 구매 일자
        editBuyDate = findViewById(R.id.editAuctionBuyDate);
        Calendar calender = Calendar.getInstance();
        editBuyDate.setText(calender.get(Calendar.YEAR) +"-"+ (calender.get(Calendar.MONTH)+1) +"-"+ calender.get(Calendar.DATE));
        editBuyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(UploadPage.this, datePickerBuyDate,
                        calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DATE));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        // 경매 종료 일자
        editEndDate = findViewById(R.id.editAuctionFinalDate);
        editEndDate.setText(calender.get(Calendar.YEAR) +"-"+ (calender.get(Calendar.MONTH)+1) +"-"+ calender.get(Calendar.DATE));
        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(UploadPage.this, datePickerEndDate,
                        calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DATE));
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
                itemStatePoint = Math.round(rating);
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

        EditText editItemName = (EditText) findViewById(R.id.editItemName);
        TextView editCategory = (TextView) findViewById(R.id.selectItemCategory);
        EditText editPrice = (EditText)findViewById(R.id.editItemStartPrice);
        EditText editContent = (EditText) findViewById(R.id.editItemContent);
        // 완료 버튼
        TextView uploadButton = (TextView) findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                // TODO: upload method
                String itemName = editItemName.getText().toString();
                //EItemCategory category = EItemCategory.valueOf(editCategory.getText().toString());
                //System.out.println("category"+category);
                String category = editCategory.getText().toString();
                String initPriceStr = editPrice.getText().toString();
                if(initPriceStr == null) {
                    System.out.println("경매시작가 입력하세요");
                    return;
                }
                int initPrice = Integer.parseInt(initPriceStr);
                String buyDate = editBuyDate.getText().toString()+" 00:00:00";
                String auctionClosingDate = editEndDate.getText().toString()+" 00:00:00";
                String description = editContent.getText().toString();

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime buyDateTime = LocalDateTime.parse(buyDate, formatter);
                DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime auctionClosingDateTime = LocalDateTime.parse(auctionClosingDate, formatter2);

                RequestBody itemNameR = RequestBody.create(MediaType.parse("text/plain"),itemName);
                RequestBody categoryR = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(category));
                RequestBody initPriceR = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(initPrice));
                RequestBody buyDateR = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(buyDateTime));
                RequestBody itemStatePointR = RequestBody.create(MediaType.parse("text/plain"),String.valueOf(itemStatePoint));
                RequestBody auctionClosingDateR = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(auctionClosingDateTime));
                RequestBody descriptionR = RequestBody.create(MediaType.parse("text/plain"),description);
                //localDateTime 이렇게 넣는게 맞는 지?
                map.put("itemName", itemNameR);
                map.put("category", categoryR);
                map.put("initPrice", initPriceR);
                map.put("buyDate", buyDateR);
                map.put("itemStatePoint", itemStatePointR);
                map.put("auctionClosingDate", auctionClosingDateR);
                map.put("description", descriptionR);
                makeMultiPart();

//                RegisterItemRequest registerItemRequest = new RegisterItemRequest(itemName, category, initPrice, buyDateTime, itemStatePoint, description, auctionClosingDateTime);

//                RegisterItemRequest registerItemRequest = new RegisterItemRequest();
                RetrofitTool.getAPIWithNullConverter().uploadItem(files, itemNameR, categoryR, initPriceR, buyDateR, itemStatePointR, auctionClosingDateR, descriptionR)
                        .enqueue(MainRetrofitTool.getCallback(new UploadPage.RegisterItemCallback()));


            }
        });

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
    public void makeMultiPart() {
        //filepath는 photoUri.getPath()
//        File file = new File(filepath);
//        InputStream inputStream = null;
//        try {
//            inputStream = getApplicationContext().getContentResolver().openInputStream(photoUri);
//        }catch(IOException e) {
//            e.printStackTrace();
//        }
//        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
//        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), byteArrayOutputStream.toByteArray());
//        files = MultipartBody.Part.createFormData("itemImg", file.getName() ,requestBody);

        for (int i = 0; i < uriList.size(); ++i) {
            // Uri 타입의 파일경로를 가지는 RequestBody 객체 생성
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), String.valueOf(uriList.get(i)));
            // 사진 파일 이름
            String fileName = "photo" + i + ".jpg";
            // RequestBody로 Multipart.Part 객체 생성
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("fileNames", fileName, fileBody);
            // 추가
            files.add(filePart);
        }

    }
    private class RegisterItemCallback implements MainRetrofitCallback<RegisterItemResponse> {
        @Override
        public void onSuccessResponse(Response<RegisterItemResponse> response) {
            RegisterItemResponse result = response.body();
            Log.d(TAG, "retrofit success, idToken: " + result.toString());

        }
        @Override
        public void onFailResponse(Response<RegisterItemResponse> response) {
            Log.d(TAG, "onFailResponse");
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e("연결실패", t.getMessage());
        }
    }
}


