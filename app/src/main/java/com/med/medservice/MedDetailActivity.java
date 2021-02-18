package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.med.medservice.Models.ProductMedicine.MedicineList;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.Utils.SessionManager;
import com.med.medservice.Utils.UpdateCartInterface;
import com.med.medservice.Utils.ViewDialog;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class MedDetailActivity extends AppCompatActivity  implements UpdateCartInterface {

    TextView ProductQuantity, ProductSubTotal;

    ImageView MedicineImageView;
    TextView MedicineNameView, MedicineShortDesc, MedicinePriceView;

    MedicineList currentData;
    String medicine_id;// = currentData.getMedicine_id();
    String medicine_name;// = currentData.getMedicine_name();
    String medicine_desc;// = currentData.getMedicine_short_desc();
    String medicine_price;// = currentData.getMedicine_regular_price();
    String medicine_image;

    CartDBHelper mydb;
    String name, user_id, email;
    SessionManager sessionManager;


    TextView cartNumberView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_med_detail);

        ProductQuantity = findViewById(R.id.productQuantityText);

        mydb = new CartDBHelper(this);
        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        email = user.get(sessionManager.EMAIL);
        user_id = user.get(sessionManager.ID);

        cartNumberView = findViewById(R.id.cartNumberView);


        GetMedicineDetails();

        SetupUI();


    }

    private void SetupUI() {

        MedicineImageView = findViewById(R.id.MedicineImageView);
        MedicineNameView = findViewById(R.id.MedicineNameView);
        MedicineShortDesc = findViewById(R.id.MedicineShortDesc);
        MedicinePriceView = findViewById(R.id.MedicinePriceView);

        Picasso.get()
                .load(medicine_image)
                // .placeholder(context.getResources().getDrawable(R.drawable.ic))
                .into(MedicineImageView, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        // if (holderr.Course_Progress != null) {
                        //      holderr.Course_Progress.setVisibility(View.GONE);
                        //  }
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
        //  holder.medicine_image_view.setText(medicine_name.substring(0, 1));

        MedicineNameView.setText(medicine_name);
        MedicineShortDesc.setText(medicine_desc);
        MedicinePriceView.setText("$"+medicine_price+".00");



    }

    private void GetMedicineDetails() {
        Intent intent = getIntent();
        currentData = (MedicineList) intent.getSerializableExtra("selectedMed");
        if (currentData != null) {
            medicine_id = currentData.getMedicine_id();
        }
        if (currentData != null) {
            medicine_name = currentData.getMedicine_name();
        }
        if (currentData != null) {
            medicine_desc = currentData.getMedicine_short_desc();
        }
        if (currentData != null) {
            medicine_price = currentData.getMedicine_regular_price();
        }
        if (currentData != null) {
            medicine_image = currentData.getMedicine_image();
        }
    }

    public void Close(View view) {
        finish();
    }

    public void DecreaseQuantity(View view) {

        String temp = ProductQuantity.getText().toString();
        if (!temp.equals("1")) {
            int existing = Integer.parseInt(temp);
            existing = existing - 1;
            temp = String.valueOf(existing);
            ProductQuantity.setText(temp);

            //  int pro_price_int = Integer.parseInt(rupee);
            //  pro_price_int = existing * pro_price_int;
            //  String pro_price_string = String.valueOf(pro_price_int);
            //    ProductSubTotal.setText(pro_price_string);

        }


    }

    public void IncreaseQuantity(View view) {

        String temp = ProductQuantity.getText().toString();
        int existing = Integer.parseInt(temp);
        existing = existing + 1;
        temp = String.valueOf(existing);
        ProductQuantity.setText(temp);

        //  int pro_price_int = Integer.parseInt(rupee);
        // pro_price_int = existing * pro_price_int;
        // String pro_price_string = String.valueOf(pro_price_int);
        //ProductSubTotal.setText(pro_price_string);

    }

    public void AddToCart(View view) {

        mydb.insertCartItem(user_id, medicine_id, medicine_name, ProductQuantity.getText().toString(), medicine_price, "0", "medicine", medicine_image);

        ViewDialog alert = new ViewDialog();
        alert.showDialog(this, "("+ProductQuantity.getText().toString()+") "+medicine_name+"\nAdded in Cart");

        UpdateCart();

    }

    public void OpenCart(View view) {
        startActivity(new Intent(getApplicationContext(), CartActivity.class));
    }

    public void OpenHome(View view) {
        Intent i = new Intent(this, PatientMainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UpdateCart();
    }

    @Override
    public void UpdateCart() {

        if (mydb.numberOfRows() > 0) {
            cartNumberView.setText("" + mydb.numberOfRows());
        } else {
            cartNumberView.setText("");
        }
    }
}