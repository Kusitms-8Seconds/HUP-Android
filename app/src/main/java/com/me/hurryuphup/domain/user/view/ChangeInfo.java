package com.me.hurryuphup.domain.user.view;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.me.hurryuphup.databinding.ActivityChangeInfoBinding;
import com.me.hurryuphup.domain.upload.constant.UploadConstants;
import com.me.hurryuphup.domain.user.presenter.ChangeInfoPresenter;

import java.io.File;

import lombok.SneakyThrows;

public class ChangeInfo extends AppCompatActivity implements ChangeInfoView{
    private ActivityChangeInfoBinding binding;
    ChangeInfoPresenter presenter;

    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangeInfoBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new ChangeInfoPresenter(this, binding, getApplicationContext());
        presenter.init();

        binding.ivProfileChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 앨범으로 이동
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true); //다중 이미지를 가져올 수 있도록
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 2222);
            }
        });
        binding.btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = null;
                if(imagePath != null) intent = presenter.UpdateCheck(imagePath);
                else showToast(UploadConstants.EUploadToast.unselectImage.getText());
                if(intent !=null){
                    startActivity(intent);
                }
            }
        });
        binding.checkId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.duplicateLoginIdCheck(binding.edtLoginId.getText().toString());
            }
        });
    }
    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
    // select image
    @SneakyThrows
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 2222){
            if(data == null){   // 어떤 이미지도 선택하지 않은 경우
                showToast(UploadConstants.EUploadToast.unselectImage.getText());
            }
            else{   // 이미지를 선택한 경우
                if(data.getClipData() != null){
                    // 이미지를 하나만 선택
                    if(data.getClipData().getItemCount() > 1) showToast(UploadConstants.EUploadToast.oneselectImage.getText());
                    else {
                        Uri imageUri = data.getData();
                        binding.ivProfileChange.setImageURI(imageUri);
                        binding.cameraIv.setVisibility(View.INVISIBLE);
                        imagePath = getRealpath(imageUri);
                        File profileFile = new File(imagePath);
                    }
                } else {
                    binding.cameraIv.setVisibility(View.VISIBLE);
                }
            }
        }
    }
    public String getRealpath(Uri uri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor c = managedQuery(uri, proj, null, null, null);
        int index = c.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

        c.moveToFirst();
        String path = c.getString(index);

        return path;
    }
}
