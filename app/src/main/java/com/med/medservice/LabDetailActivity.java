package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.med.medservice.Models.ProductLabs.LabsList;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.Utils.SessionManager;
import com.med.medservice.Utils.UpdateCartInterface;
import com.med.medservice.Utils.ViewDialog;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class LabDetailActivity extends AppCompatActivity implements UpdateCartInterface {

    TextView ProductQuantity, ProductSubTotal;

    ImageView labsImageView;
    TextView labsNameView, labsShortDesc, labsPriceView;

    LabsList currentData;
    String lab_id;// = currentData.getMedicine_id();
    String lab_name;// = currentData.getMedicine_name();
    String lab_desc;// = currentData.getMedicine_short_desc();
    String lab_price;// = currentData.getMedicine_regular_price();
    String lab_image;

    CartDBHelper mydb;
    String name, user_id, email;
    SessionManager sessionManager;


    TextView cartNumberView;

    LinearLayout panelTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ViewConfiguration.get(getApplicationContext()).hasPermanentMenuKey();
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lab_detail);

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

    private void GetMedicineDetails() {
        Intent intent = getIntent();
        currentData = (LabsList) intent.getSerializableExtra("selectedLab");
        if (currentData != null) {
            lab_id = currentData.getLab_id();
        }
        if (currentData != null) {
            lab_name = currentData.getLab_name();
        }
        if (currentData != null) {
            lab_desc = currentData.getLab_short_desc();
        }
        if (currentData != null) {
            lab_price = currentData.getLab_sale_price();
        }
        if (currentData != null) {
            lab_image = currentData.getLab_image();
        }
    }

    private void SetupUI() {

        labsImageView = findViewById(R.id.labsImageView);
        labsNameView = findViewById(R.id.labsNameView);
        labsShortDesc = findViewById(R.id.labsShortDesc);
        labsPriceView = findViewById(R.id.labsPriceView);
        panelTest = findViewById(R.id.panelTest);


        Picasso.get()
                .load(lab_image)
                // .placeholder(context.getResources().getDrawable(R.drawable.ic))
                .into(labsImageView, new com.squareup.picasso.Callback() {
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

        labsNameView.setText(lab_name);
        String sentence = labsNameView.getText().toString();
        String search  = "Panel";

        if ( sentence.toLowerCase().indexOf(search.toLowerCase()) != -1 ) {
            System.out.println("I found the keyword");
        }
        else {
            panelTest.setVisibility(View.GONE);
        }

        labsPriceView.setText("$"+ lab_price +".00");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            labsShortDesc.setText(Html.fromHtml(lab_desc, Html.FROM_HTML_MODE_COMPACT));
        } else {

            labsShortDesc.setText(Html.fromHtml(lab_desc));
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

        mydb.insertCartItem(user_id, lab_id, lab_name, ProductQuantity.getText().toString(), lab_price, "0", "lab-test", lab_image);

        ViewDialog alert = new ViewDialog();
        alert.showDialog(this, "("+ProductQuantity.getText().toString()+") "+ lab_name +"\nAdded in Cart");

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