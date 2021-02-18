package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.med.medservice.Utils.CartDBHelper;

import java.util.regex.Pattern;

public class CheckoutActivity extends AppCompatActivity {

    LinearLayout payment_done_layout;
    EditText date_expiry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_checkout);

        payment_done_layout = findViewById(R.id.payment_done_layout);

        date_expiry = findViewById(R.id.date_expiry);
        date_expiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                int length = date_expiry.getText().length();

              //  Pattern p = Pattern.compile("\\d{2}-\\d{2}");  // use a better name, though


                if (length == 4) {


                    String temp = date_expiry.getText().toString();
                    String[] result = temp.split("(?!^)");
                    if (!result[2].equals("/")) {
                        date_expiry.setText(result[0] + result[1] + "/" + result[2] + result[3]);
                    }else {
                        date_expiry.setText("");
                    }
                }
            }
        });
    }

    public void Close(View view) {
        finish();
    }

    public void ConfirmOrder(View view) {
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);


        payment_done_layout.setVisibility(View.VISIBLE);
        payment_done_layout.startAnimation(slide_up);
        EmptyCart();

        //august_month_check = true;

    }

    private void EmptyCart() {
        CartDBHelper mydb;
        mydb = new CartDBHelper(this);
        mydb.removeAllItems(0);

    }


    @Override
    public void onBackPressed() {

    }

    public void OpenPharmacy(View view) {
        Intent intent = new Intent(this, PatientMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    public void OpenHome(View view) {
        Intent i = new Intent(this, PatientMainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }

    public void SelectDateTime(View view) {
    }
}