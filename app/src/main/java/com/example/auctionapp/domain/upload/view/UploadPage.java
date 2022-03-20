package com.example.auctionapp.domain.upload.view;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.DialogInterface;
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
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.auctionapp.MainActivity;
import com.example.auctionapp.R;
import com.example.auctionapp.databinding.ActivityUploadPageBinding;
import com.example.auctionapp.domain.upload.adapter.MultiImageAdapter;
import com.example.auctionapp.domain.upload.constant.UploadConstants;
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
    UploadPresenter presenter;

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
            String formatDate = endDateTime.format(DateTimeFormatter.ofPattern(UploadConstants.EDate.datePattern.getText()));
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
            String formatDate = endDateTime.format(DateTimeFormatter.ofPattern(UploadConstants.EDate.datePattern.getText()));
            binding.editAuctionFinalDate.setText(formatDate.toString());
        }
    };
    TimePickerDialog.OnTimeSetListener timePickerEndTime = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            String hourStr = String.valueOf(hourOfDay);
            String minStr = String.valueOf(minute);
            if(hourOfDay < 10) hourStr = "0" + hourOfDay;
            if(minute < 10) minStr = "0" + minute;
            binding.editAuctionFinalTime.setText(hourStr + ":" + minStr + ":00");
        }
    };

    private static final String TAG = "UploadPage";
    ArrayList<Uri> uriList;   // 이미지의 uri를 담을 ArrayList 객체
    ArrayList<File> fileList;

    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    int itemStatePoint; //item rating
    ArrayList<MultipartBody.Part> files = new ArrayList<>(); // 여러 file들을 담아줄 ArrayList

    static final String[] LIST_MENU = {"디지털 기기", "생활가전", "가구/인테리어", "유아동", "생활/가공식품"
            ,"유아도서", "스포츠/레저", "여성잡화", "여성의류", "남성패션/잡화", "게임/취미", "뷰티/미용",
            "반려동물용품", "도서/티켓/음반", "식물"} ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadPageBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new UploadPresenter(this, binding, getApplicationContext());

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
        // 경매 종료 시간
        binding.editAuctionFinalTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimePickerDialog dialog = new TimePickerDialog(UploadPage.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
                        timePickerEndTime, 00, 00, true);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.show();
            }
        });

        // 물건 상태
        binding.itemStateRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getApplicationContext(), UploadConstants.EUploadLog.newRating.getText() + rating, Toast.LENGTH_SHORT).show();
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
                AlertDialog.Builder dlg = new AlertDialog.Builder(UploadPage.this);
                dlg.setTitle("카테고리 선택"); //제목
                dlg.setIcon(R.drawable.interest); // 아이콘 설정

                dlg.setItems(LIST_MENU, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        binding.selectItemCategory.setText(LIST_MENU[which]);
                    }
                });
                dlg.show();
            }
        });

        // 완료 버튼
        binding.uploadButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                if(Constants.userId == null) {
                    showToast(UploadConstants.EUploadToast.afterLogin.getText());
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
                        showToast(UploadConstants.EUploadToast.editInitPrice.getText());
                        return;
                    }
                    int initPrice = Integer.parseInt(initPriceStr);
                    String buyDate = binding.editAuctionBuyDate.getText().toString() + UploadConstants.EDate.dateZero.getText();
                    String auctionClosingDate = binding.editAuctionFinalDate.getText().toString() + " " + binding.editAuctionFinalTime.getText().toString();
                    String description = binding.editItemContent.getText().toString();

                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(UploadConstants.EDate.dateTimePattern.getText());
                    LocalDateTime buyDateTime = LocalDateTime.parse(buyDate, formatter);
                    DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern(UploadConstants.EDate.dateTimePattern.getText());
                    LocalDateTime auctionClosingDateTime = LocalDateTime.parse(auctionClosingDate, formatter2);

                    RequestBody userIdR = RequestBody.create(MediaType.parse(UploadConstants.EMultiPart.mediaTypePlain.getText()), String.valueOf(Constants.userId));
                    RequestBody itemNameR = RequestBody.create(MediaType.parse(UploadConstants.EMultiPart.mediaTypePlain.getText()), itemName);
                    RequestBody categoryR = RequestBody.create(MediaType.parse(UploadConstants.EMultiPart.mediaTypePlain.getText()), presenter.choiceCategory(binding.selectItemCategory.getText().toString()));
                    RequestBody initPriceR = RequestBody.create(MediaType.parse(UploadConstants.EMultiPart.mediaTypePlain.getText()), String.valueOf(initPrice));
                    RequestBody buyDateR = RequestBody.create(MediaType.parse(UploadConstants.EMultiPart.mediaTypePlain.getText()), String.valueOf(buyDateTime));
                    RequestBody itemStatePointR = RequestBody.create(MediaType.parse(UploadConstants.EMultiPart.mediaTypePlain.getText()), String.valueOf(itemStatePoint));
                    RequestBody auctionClosingDateR = RequestBody.create(MediaType.parse(UploadConstants.EMultiPart.mediaTypePlain.getText()), String.valueOf(auctionClosingDateTime));
                    RequestBody descriptionR = RequestBody.create(MediaType.parse(UploadConstants.EMultiPart.mediaTypePlain.getText()), description);

                    makeMultiPart();
                    System.out.println(UploadConstants.EUploadLog.fileCheck.getText() + files.toString());
                    presenter.upload(files, userIdR,
                            itemNameR, categoryR, initPriceR, buyDateR, itemStatePointR,
                            auctionClosingDateR, descriptionR);
                    //go home
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
                showToast(UploadConstants.EUploadToast.unselectImage.getText());
            }
            else{   // 이미지를 하나라도 선택한 경우
                if(data.getClipData() == null){     // 이미지를 하나만 선택한 경우
                    Log.e(UploadConstants.EUploadLog.singleChoice.getText(), String.valueOf(data.getData()));
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
                        showToast(UploadConstants.EUploadToast.imageSelectOver.getText());
                    }
                    else{   // 선택한 이미지가 1장 이상 10장 이하인 경우
                        Log.e(TAG, UploadConstants.EUploadLog.multiChoice.getText());

                        for (int i = 0; i < clipData.getItemCount(); i++){
                            Uri imageUri = clipData.getItemAt(i).getUri();  // 선택한 이미지들의 uri를 가져온다.

                            try {
                                uriList.add(imageUri);  //uri를 list에 담는다.
                                String imagePath = getRealpath(imageUri);
                                File destFile = new File(imagePath);
                                fileList.add(destFile);
                            } catch (Exception e) {
                                Log.e(TAG, UploadConstants.EUploadLog.fileSelectError.getText(), e);
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
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void makeMultiPart() {
        for (int i = 0; i < fileList.size(); ++i) {
            // Uri 타입의 파일경로를 가지는 RequestBody 객체 생성

//            System.out.println("fileList.get(0)"+fileList.get(0).toString());
            RequestBody fileBody = RequestBody.create(MediaType.parse(UploadConstants.EMultiPart.mediaTypeImage.getText()), fileList.get(0));
            // 사진 파일 이름
            LocalDateTime localDateTime = LocalDateTime.now();
            String fileName = "photo" + localDateTime + ".jpg";
            // RequestBody로 Multipart.Part 객체 생성
            MultipartBody.Part filePart = MultipartBody.Part.createFormData(UploadConstants.EMultiPart.files.getText(), fileName, fileBody);
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


