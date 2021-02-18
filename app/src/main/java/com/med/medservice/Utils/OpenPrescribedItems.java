package com.med.medservice.Utils;

import android.content.Context;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.DoctorVideoActivity;
import com.med.medservice.Models.CartItems.CartItemsList;
import com.med.medservice.Models.CartItems.CartPrescriptionAdapter;
import com.med.medservice.PrescriptionActivity;
import com.med.medservice.R;

import java.util.ArrayList;

public class OpenPrescribedItems {


    CartDBHelper mydb;

    RecyclerView cartRecycler;
    ArrayList<CartItemsList> cartItemsLists;

    Context context;

    public OpenPrescribedItems(Context mContext, String Activity) {

        context = mContext;

        mydb = new CartDBHelper(context);
        if (mydb.numberOfRows() < 1) {
            //  mydb.insertCartItem("1", "2", "Carline", "4", "55", "66", "medicine", "URL");
            //   mydb.insertCartItem("1", "3", "Name4", "5", "66", "77", "medicine", "URL");
        }
        cartItemsLists = mydb.getAllItems();

        if (Activity.equals("DoctorVideoActivity")) {
            cartRecycler = (RecyclerView) ((DoctorVideoActivity) context).findViewById(R.id.cartRecycler);
        }
        else if (Activity.equals("PrescriptionActivity")){
            cartRecycler = (RecyclerView) ((PrescriptionActivity) context).findViewById(R.id.cartRecycler);
        }

        // cartRecycler = context.findViewById(R.id.cartRecycler);
        cartRecycler.setLayoutManager(new LinearLayoutManager(context));

        CartPrescriptionAdapter adapter = new CartPrescriptionAdapter(cartItemsLists, context, Activity);
        cartRecycler.setAdapter(adapter);


    }
}
