package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.med.medservice.Models.OnlineDoctors.OnlineDoctorsList;

public class CreditCardPaymentActivity extends AppCompatActivity {

    OnlineDoctorsList currentData;
    String symptoms_id, desc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_credit_card_payment);


        Intent intent = getIntent();
        currentData = (OnlineDoctorsList) intent.getSerializableExtra("selectedDoctor");
        symptoms_id = intent.getStringExtra("symptoms_id");
        desc = intent.getStringExtra("desc");
    }

    public void Close(View view) {
        finish();
    }

    public void ProcessPayment(View view) {

        Intent intent = new Intent(CreditCardPaymentActivity.this, SendInvitationActivity.class);
        intent.putExtra("symptoms_id", symptoms_id);
        intent.putExtra("desc", desc);
        intent.putExtra("selectedDoctor", currentData);
        startActivity(intent);

        //startActivity(new Intent(CreditCardPaymentActivity.this, SendInvitationActivity.class));
        finish();
    }
}