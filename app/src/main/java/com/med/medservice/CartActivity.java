package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.med.medservice.Models.CartItems.CartAdapter;
import com.med.medservice.Models.CartItems.CartItemsList;
import com.med.medservice.Models.ProductMedicine.MedicineAdapter;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.Utils.ViewDialog;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    CartDBHelper mydb;

    RecyclerView cartRecycler;
    ArrayList<CartItemsList> cartItemsLists;

    int Total = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_cart);

        mydb = new CartDBHelper(this);
        if (mydb.numberOfRows() < 1) {
            //  mydb.insertCartItem("1", "2", "Carline", "4", "55", "66", "medicine", "URL");
            //   mydb.insertCartItem("1", "3", "Name4", "5", "66", "77", "medicine", "URL");
        }
        cartItemsLists = mydb.getAllItems();


        cartRecycler = findViewById(R.id.cartRecycler);
        cartRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        CartAdapter adapter = new CartAdapter(cartItemsLists, CartActivity.this);
        cartRecycler.setAdapter(adapter);

        CalculateTotal(cartItemsLists);


        final LottieAnimationView view = findViewById(R.id.animation_view);

        view.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {


            }

            @Override
            public void onAnimationEnd(Animator animator) {


            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    public void CalculateTotal(ArrayList<CartItemsList> cartItemsLists) {


        TextView subTotalPrice = findViewById(R.id.subTotalPrice);
        TextView totalPrice = findViewById(R.id.totalPrice);
        TextView shippingTextView = findViewById(R.id.shippingTextView);


        for (int i = 0; i < cartItemsLists.size(); i++) {

            CartItemsList currentData = cartItemsLists.get(i);

            String quantity = currentData.getQUANTITY();
            String price = currentData.getPRICE();

            try {

                Total = Total + ((Integer.parseInt(price)) * (Integer.parseInt(quantity)));

            } catch (NumberFormatException e) {

            }

        }

        if (Total < 1) {
            shippingTextView.setText("$00.00");
            totalPrice.setText("$" + (Total) + ".00");
            subTotalPrice.setText("$" + Total + ".00");

            //ViewDialog alert = new ViewDialog();
            // alert.showDialog(CartActivity.this, "Cart is empty");

            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.text_dialog_ok);
            TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
            text.setText("Cart is empty");
            Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
            dialogButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    finish();
                }
            });
            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);


        } else {

            subTotalPrice.setText("$" + Total + ".00");
            totalPrice.setText("$" + (Total + 20) + ".00");
        }

    }

    public void Close(View view) {
        finish();
    }

    public void OpenCheckout(View view) {
        Intent intent= new Intent(getApplicationContext(), CheckoutActivity.class);
        intent.putExtra("price", Total);
        finish();
        startActivity(intent);
    }

    public void OpenHome(View view) {
        Intent i = new Intent(this, PatientMainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }

    public void DeleteAllItems(View view) {

        final Dialog dialog = new Dialog(CartActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.text_dialog_ok);

        TextView text = (TextView) dialog.findViewById(R.id.text_dialog);
        text.setText("Cart Empty");

        Button dialogButton = (Button) dialog.findViewById(R.id.btn_dialog);
        mydb.removeAllItems(0);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                finish();
            }
        });

        dialog.show();

        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

    }
}