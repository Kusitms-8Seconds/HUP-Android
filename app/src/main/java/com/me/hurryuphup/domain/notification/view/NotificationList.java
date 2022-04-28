package com.me.hurryuphup.domain.notification.view;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.me.hurryuphup.databinding.ActivityNotificationListBinding;
import com.me.hurryuphup.domain.notification.presenter.NotificationListPresenter;
import com.me.hurryuphup.domain.user.constant.Constants;


public class NotificationList extends AppCompatActivity implements NotificationListView{
    private ActivityNotificationListBinding binding;
    private NotificationListPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotificationListBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        presenter = new NotificationListPresenter(this, binding, getApplicationContext());
        presenter.init(Constants.userId);
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }
}
