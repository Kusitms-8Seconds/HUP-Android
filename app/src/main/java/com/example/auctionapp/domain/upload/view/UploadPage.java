package com.example.auctionapp.domain.upload.view;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityUploadPageBinding;
import com.example.auctionapp.domain.upload.adapter.MultiImageAdapter;
import com.example.auctionapp.domain.upload.presenter.UploadPresenter;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.domain.item.dto.RegisterItemResponse;
import com.example.auctionapp.global.retrofit.RetrofitTool;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class UploadPage extends AppCompatActivity implements UploadView{
    private ActivityUploadPageBinding binding;
    UploadPresenter presenter = new UploadPresenter(this);

    // DatePickerDialog
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener datePickerBuyDate = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            LocalDateTime endDateTime = LocalDateTime.of(year, month+1, dayOfMonth, 00, 00,00,0000);
            String formatDate = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            binding.editAuctionBuyDate.setText(formatDate.toString());
        }
    };
    DatePickerDialog.OnDateSetListener datePickerEndDate = new DatePickerDialog.OnDateSetListener() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            LocalDateTime endDateTime = LocalDateTime.of(year, month+1, dayOfMonth, 00, 00,00,0000);
            String formatDate = endDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            binding.editAuctionFinalDate.setText(formatDate.toString());
        }
    };

    private static final String TAG = "UploadPage";
    ArrayList<Uri> uriList;   // 이미지의 uri를 담을 ArrayList 객체
    ArrayList<File> fileList;
//    RecyclerView selectedImageRecyclerView;  // 이미지를 보여줄 리사이클러뷰
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    int itemStatePoint; //item rating
    ArrayList<MultipartBody.Part> files = new ArrayList<>(); // 여러 file들을 담아줄 ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent tt = new Intent(UploadPage.this, MainActivity.class);
                tt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(tt);
            }
        });

        // 구매 일자
        Calendar calender = Calendar.getInstance();
        binding.editAuctionBuyDate.setText(calender.get(Calendar.YEAR) +"-"+ (calender.get(Calendar.MONTH)+1) +"-"+ calender.get(Calendar.DATE));
        binding.editAuctionBuyDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(UploadPage.this, datePickerBuyDate,
                        calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DATE));
                dialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        // 경매 종료 일자
        binding.editAuctionFinalDate.setText(calender.get(Calendar.YEAR) +"-"+ (calender.get(Calendar.MONTH)+1) +"-"+ calender.get(Calendar.DATE));
        binding.editAuctionFinalDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(UploadPage.this, datePickerEndDate,
                        calender.get(Calendar.YEAR), calender.get(Calendar.MONTH), calender.get(Calendar.DATE));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis());
                dialog.show();
            }
        });

        // 물건 상태
        binding.itemStateRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getApplicationContext(),"New Rating: "+ rating, Toast.LENGTH_SHORT).show();
                itemStatePoint = Math.round(rating);
            }
        });

        // 이미지 업로드
        binding.chooseImage.setOnClickListener(new View.OnClickListener() {
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
                tt.putExtra("itemName", binding.editItemName.getText().toString());
                tt.putExtra("itemPrice", binding.editItemStartPrice.getText().toString());
                tt.putExtra("itemContent", binding.editItemContent.getText().toString());
                tt.putExtra("itemBuyDate", binding.editAuctionBuyDate.getText().toString());
                tt.putExtra("itemEndDate", binding.editAuctionFinalDate.getText().toString());
                tt.putExtra("itemStatePoint", itemStatePoint);
                startActivity(tt);
            }
        });
        Intent getCategoryIntent = getIntent();
        String itemCT = getCategoryIntent.getStringExtra("itemCategory");
        String ii = getCategoryIntent.getStringExtra("itemName");
        String ii2 = getCategoryIntent.getStringExtra("itemPrice");
        String ii3 = getCategoryIntent.getStringExtra("itemContent");
        String ii4 = getCategoryIntent.getStringExtra("itemBuyDate");
        String ii5 = getCategoryIntent.getStringExtra("itemEndDate");
        int ii6 = getCategoryIntent.getIntExtra("itemStatePoint", 0);
        binding.editItemName.setText(ii);
        binding.selectItemCategory.setText(itemCT);
        binding.editItemStartPrice.setText(ii2);
        binding.editItemContent.setText(ii3);
        binding.editAuctionBuyDate.setText(ii4);
        binding.editAuctionFinalDate.setText(ii5);
        binding.itemStateRatingBar.setRating(ii6);

        // 완료 버튼
        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(Constants.userId == null) {
                    Toast.makeText(getApplicationContext(), "로그인 후 상품 등록이 가능합니다.", Toast.LENGTH_SHORT).show();
                } else {
                    String itemName = binding.editItemName.getText().toString();
//                Iterator<EItemCategory> iterator = Arrays.stream(EItemCategory.values()).iterator();
//                while(iterator.hasNext()) {
//                    EItemCategory next = iterator.next();
//                    if(next.getName().equals(editCategory.getText().toString())){
//                        selectedCategory = next; }
//                }

                    String initPriceStr = binding.editItemStartPrice.getText().toString();
                    if (initPriceStr == null) {
                        System.out.println("경매시작가 입력하세요");
                        return;
                    }
                    int initPrice = Integer.parseInt(initPriceStr);
                    String buyDate = binding.editAuctionBuyDate.getText().toString() + " 00:00:00";
                    String auctionClosingDate = binding.editAuctionFinalDate.getText().toString() + " 00:00:00";
                    String description = binding.editItemContent.getText().toString();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime buyDateTime = LocalDateTime.parse(buyDate, formatter);
                    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime auctionClosingDateTime = LocalDateTime.parse(auctionClosingDate, formatter2);

                    RequestBody userIdR = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(Constants.userId));
                    RequestBody itemNameR = RequestBody.create(MediaType.parse("text/plain"), itemName);
                    RequestBody categoryR = RequestBody.create(MediaType.parse("text/plain"), presenter.choiceCategory(binding.selectItemCategory.getText().toString()));
                    RequestBody initPriceR = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(initPrice));
                    RequestBody buyDateR = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(buyDateTime));
                    RequestBody itemStatePointR = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(itemStatePoint));
                    RequestBody auctionClosingDateR = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(auctionClosingDateTime));
                    RequestBody descriptionR = RequestBody.create(MediaType.parse("text/plain"), description);

                    makeMultiPart();
                    System.out.println("files체크" + files.toString());
                    presenter.upload(files, userIdR,
                            itemNameR, categoryR, initPriceR, buyDateR, itemStatePointR,
                            auctionClosingDateR, descriptionR);
                    //go home
                    Toast.makeText(getApplicationContext(), "상품 등록이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent tt = new Intent(getApplicationContext(), MainActivity.class);
                    tt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(tt);
                }
            }

        });

    }

    // select image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //uriList.clear();    // 이미지 다시 선택 시 초기화
        uriList = new ArrayList<>();
        fileList = new ArrayList<>();
        if(requestCode == 2222){
            if(data == null){   // 어떤 이미지도 선택하지 않은 경우
                Toast.makeText(getApplicationContext(), "이미지를 선택하지 않았습니다.", Toast.LENGTH_LONG).show();
            }
            else{   // 이미지를 하나라도 선택한 경우
                if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                    Log.e("single choice: ", String.valueOf(data.getData()));
                    binding.selectedImageCount.setText("1/10");
                    Uri imageUri = data.getData();
                    uriList.add(imageUri);
                    String imagePath = getRealpath(imageUri);
                    File destFile = new File(imagePath);
                    fileList.add(destFile);
                    adapter = new MultiImageAdapter(uriList, getApplicationContext());
                    binding.selectedImageRecyclerView.setAdapter(adapter);
                    binding.selectedImageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));
                }
                else{      // 이미지를 여러장 선택한 경우
                    ClipData clipData = data.getClipData();
                    binding.selectedImageCount.setText(String.valueOf(clipData.getItemCount()) + "/10");

                    if(clipData.getItemCount() > 10){   // 선택한 이미지가 11장 이상인 경우
                        Toast.makeText(getApplicationContext(), "사진은 10장까지 선택 가능합니다.", Toast.LENGTH_LONG).show();
                    }
                    else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                        Log.e(TAG, "multiple choice");

                        for (int i = 0; i < clipData.getItemCount(); i++){
                            Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.

                            try {
                                uriList.add(imageUri);  //uri를 list에 담는다.
                                String imagePath = getRealpath(imageUri);
                                File destFile = new File(imagePath);
                                fileList.add(destFile);
                            } catch (Exception e) {
                                Log.e(TAG, "File select error", e);
                            }
                        }
                        adapter = new MultiImageAdapter(uriList, getApplicationContext());
                        binding.selectedImageRecyclerView.setAdapter(adapter);   // 리사이클러뷰에 어댑터 세팅
                        binding.selectedImageRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true));     // 리사이클러뷰 수평 스크롤 적용
                    }
                }
            }
        }
    }
    @Override
    public void makeMultiPart() {
        for (int i = 0; i < fileList.size(); ++i) {
            // Uri 타입의 파일경로를 가지는 RequestBody 객체 생성

            System.out.println("fileList.get(0)"+fileList.get(0).toString());
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/jpeg"), fileList.get(0));
            // 사진 파일 이름
            LocalDateTime localDateTime = LocalDateTime.now();
            String fileName = "photo" + localDateTime + ".jpg";
            // RequestBody로 Multipart.Part 객체 생성
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("files", fileName, fileBody);
            // 추가
            files.add(filePart);
        }

    }

    @Override
    public String getRealpath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor c = managedQuery(uri, proj, null, null, null);
        int index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        c.moveToFirst();
        String path = c.getString(index);

        return path;
    }
}


