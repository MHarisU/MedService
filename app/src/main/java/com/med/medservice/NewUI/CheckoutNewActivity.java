package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.med.medservice.R;

public class CheckoutNewActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    Switch shippingAddressSwitch;
    Button addCardBtn;

    LinearLayout shippingLayout, addCardLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_checkout_new);

        addCardLayout = findViewById(R.id.addNewCardLayout);


        shippingAddressSwitch = findViewById(R.id.shippingAddressSwitch);
        shippingAddressSwitch.setOnCheckedChangeListener(this);

        shippingLayout = findViewById(R.id.shippingAddressLayout);

        addCardBtn = findViewById(R.id.addCardButton);
        addCardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (addCardLayout.getVisibility() == View.VISIBLE) {
                    addCardLayout.setVisibility(View.GONE);
                } else {
                    addCardLayout.setVisibility(View.VISIBLE);
                }
            }

        });

    }

    public void GoBackToCart(View view) {
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            shippingLayout.setVisibility(View.VISIBLE);
        } else {
            shippingLayout.setVisibility(View.GONE);
        }
    }
}