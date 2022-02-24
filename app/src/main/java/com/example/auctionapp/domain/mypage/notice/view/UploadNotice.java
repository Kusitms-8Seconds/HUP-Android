package com.example.auctionapp.domain.mypage.notice.view;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.auctionapp.MainActivity;
import com.example.auctionapp.databinding.ActivityUploadNoticeBinding;
import com.example.auctionapp.domain.item.dto.RegisterItemResponse;
import com.example.auctionapp.domain.mypage.notice.dto.NoticeResponse;
import com.example.auctionapp.domain.upload.adapter.MultiImageAdapter;
import com.example.auctionapp.domain.upload.constant.UploadConstants;
import com.example.auctionapp.domain.upload.presenter.UploadPresenter;
import com.example.auctionapp.domain.user.constant.Constants;
import com.example.auctionapp.global.retrofit.MainRetrofitCallback;
import com.example.auctionapp.global.retrofit.MainRetrofitTool;
import com.example.auctionapp.global.retrofit.RetrofitTool;
import com.example.auctionapp.global.util.ErrorMessageParser;

import org.json.JSONException;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class UploadNotice extends AppCompatActivity {
    private ActivityUploadNoticeBinding binding;

    private static final String TAG = "UploadNotice";
    ArrayList<Uri> uriList;   // 이미지의 uri를 담을 ArrayList 객체
    ArrayList<File> fileList;
    MultiImageAdapter adapter;  // 리사이클러뷰에 적용시킬 어댑터
    ArrayList<MultipartBody.Part> files = new ArrayList<>(); // 여러 file들을 담아줄 ArrayList

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadNoticeBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

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

        //완료버튼
        binding.completeB.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                String noticeTitle = binding.editNoticeTitle.getText().toString();
                String noticeContent = binding.editNoticeContent.getText().toString();
                if(noticeTitle != null && noticeContent != null) {
                    RequestBody userIdR = RequestBody.create(MediaType.parse(UploadConstants.EMultiPart.mediaTypePlain.getText()), String.valueOf(Constants.userId));
                    RequestBody noticeTitleR = RequestBody.create(MediaType.parse(UploadConstants.EMultiPart.mediaTypePlain.getText()), noticeTitle);
                    RequestBody noticeContentR = RequestBody.create(MediaType.parse(UploadConstants.EMultiPart.mediaTypePlain.getText()), noticeContent);

                    if(fileList == null) {
                        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).uploadNotice(files , noticeTitleR, noticeContentR, userIdR)
                                .enqueue(MainRetrofitTool.getCallback(new UploadNoticeCallback()));
                        System.out.println("null img");
                    }
                    else {
                        makeMultiPart();
                        RetrofitTool.getAPIWithAuthorizationToken(Constants.accessToken).uploadNotice(files, noticeTitleR, noticeContentR, userIdR)
                                .enqueue(MainRetrofitTool.getCallback(new UploadNoticeCallback()));
                    }

                    //go home
                    Intent tt = new Intent(getApplicationContext(), MainActivity.class);
                    tt.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(tt);
                }
                else {
                    showToast("null");
                }
            }

        });

    }
    public void makeMultiPart() {
        if(fileList != null) {
            for (int i = 0; i < fileList.size(); ++i) {
                // Uri 타입의 파일경로를 가지는 RequestBody 객체 생성
                RequestBody fileBody = RequestBody.create(MediaType.parse(UploadConstants.EMultiPart.mediaTypeImage.getText()), fileList.get(i));
                // 사진 파일 이름
                LocalDateTime localDateTime = LocalDateTime.now();
                String fileName = "photo" + localDateTime + ".jpg";
                // RequestBody로 Multipart.Part 객체 생성
                MultipartBody.Part filePart = MultipartBody.Part.createFormData(UploadConstants.EMultiPart.files.getText(), fileName, fileBody);
                // 추가
                files.add(filePart);
            }
        } else if(fileList == null) return;
    }
    // select image
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //uriList.clear();    // 이미지 다시 선택 시 초기화
        uriList = new ArrayList<>();
        fileList = new ArrayList<>();
        if(requestCode == 2222){
//            if(data == null){   // 어떤 이미지도 선택하지 않은 경우
//                showToast(UploadConstants.EUploadToast.unselectImage.getText());
//            }
            if(data != null) {   // 이미지를 하나라도 선택한 경우
                if (data.getClipData() != null) {
                    ClipData clipData = data.getClipData();
                    binding.selectedImageCount.setText(String.valueOf(clipData.getItemCount()) + "/10");

                    if (clipData.getItemCount() > 10) {   // 선택한 이미지가 11장 이상인 경우
                        showToast(UploadConstants.EUploadToast.imageSelectOver.getText());
                    } else {   // 선택한 이미지가 1장 이상 10장 이하인 경우
                        Log.e(TAG, UploadConstants.EUploadLog.multiChoice.getText());

                        for (int i = 0; i < clipData.getItemCount(); i++) {
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
//    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }
    public String getRealpath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor c = managedQuery(uri, proj, null, null, null);
        int index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        c.moveToFirst();
        String path = c.getString(index);

        return path;
    }
    private class UploadNoticeCallback implements MainRetrofitCallback<NoticeResponse> {
        @Override
        public void onSuccessResponse(Response<NoticeResponse> response) {
            NoticeResponse result = response.body();
            showToast("공지사항 등록완료");
            Log.d("UploadNotice", "공지사항 등록: " + result.toString());

        }
        @Override
        public void onFailResponse(Response<NoticeResponse> response) throws IOException, JSONException {
//            ErrorMessageParser errorMessageParser = new ErrorMessageParser(response.errorBody().string());
//            showToast(errorMessageParser.getParsedErrorMessage());
            Log.d("create notice", response.errorBody().string());
        }
        @Override
        public void onConnectionFail(Throwable t) {
            Log.e(UploadConstants.EUploadCallback.rtConnectionFail.getText(), t.getMessage());
        }
    }
}
