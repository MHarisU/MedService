package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.med.medservice.R;

public class DigestiveHealthLabTestsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digestive_health_lab_tests);
    }

    public void close(View view) {
        finish();
    }
}