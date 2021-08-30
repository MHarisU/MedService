package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class ImagingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_imaging);
    }

    public void Close(View view) {
        finish();
    }

    public void OpenCart(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
    }

    public void openSearch(View view) {
        Intent intent = new Intent(ImagingActivity.this, SearchActivity.class);
        intent.putExtra("from", "pharmacy");
        startActivity(intent);
    }
}