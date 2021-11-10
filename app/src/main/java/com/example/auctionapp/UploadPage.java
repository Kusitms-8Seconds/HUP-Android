package com.example.auctionapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

import java.util.Calendar;

public class UploadPage extends AppCompatActivity {

    // DatePickerDialog
    TextView editDate;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            editDate.setText(String.format("%d-%d-%d", year, month+1, dayOfMonth));
        }
    };

    private RatingBar ratingbar;
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
                onBackPressed();
            }
        });

        // 완료 버튼
        TextView uploadButton = (TextView) findViewById(R.id.uploadButton);
        uploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: upload method
                String name = itemName.getText().toString();
                String price = itemPrice.getText().toString();
                String content = itemContent.getText().toString();
            }
        });

        // 경매 종료 일자
        editDate = findViewById(R.id.editAuctionFinalDate);
        Calendar cal = Calendar.getInstance();
        editDate.setText(cal.get(Calendar.YEAR) +"-"+ (cal.get(Calendar.MONTH)+1) +"-"+ cal.get(Calendar.DATE));
        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(UploadPage.this, myDatePicker,
                        cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DATE));
                dialog.show();
            }
        });

        ratingbar = findViewById(R.id.itemStateRatingBar);
        ratingbar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                Toast.makeText(getApplicationContext(),"New Rating: "+ rating, Toast.LENGTH_SHORT).show();
                String str = String.valueOf(rating);
//                str = Float.toString(rating);
//                textView2.setText(str);
            }
        });;

    }
}

