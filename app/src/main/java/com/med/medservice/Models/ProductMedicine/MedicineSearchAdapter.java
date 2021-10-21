package com.med.medservice.Models.ProductMedicine;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.MedDetailActivity;
import com.med.medservice.R;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;
import com.med.medservice.Utils.UpdateCartInterface;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class MedicineSearchAdapter extends RecyclerView.Adapter<MedicineSearchAdapter.MedicineHolder> {


    ArrayList<MedicineList> list;
    Context context;
    View view;


    CartDBHelper mydb;


    String name, user_id, email;
    SessionManager sessionManager;

    UpdateCartInterface cartUpdateContext;

    boolean checkAdded = false;
    String session_id;


    public MedicineSearchAdapter() {
    }

 /*   public MedicineSearchAdapter(ArrayList<MedicineList> list, Context context, UpdateCartInterface cartUpdateContext) {
        this.list = list;
        this.context = context;
        this.cartUpdateContext = cartUpdateContext;

        mydb = new CartDBHelper(context);
        sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        email = user.get(sessionManager.EMAIL);
        user_id = user.get(sessionManager.ID);

    }

    public MedicineSearchAdapter(ArrayList<MedicineList> list, Context context) {
        this.list = list;
        this.context = context;

        mydb = new CartDBHelper(context);
        sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        email = user.get(sessionManager.EMAIL);
        user_id = user.get(sessionManager.ID);
    }*/

    public MedicineSearchAdapter(ArrayList<MedicineList> list, Context context, String session_id) {
        this.list = list;
        this.context = context;
        this.session_id = session_id;

        mydb = new CartDBHelper(context);
        sessionManager = new SessionManager(context);
        HashMap<String, String> user = sessionManager.getUserDetail();
        name = user.get(sessionManager.FIRST_NAME);
        name = name + " " + user.get(sessionManager.LAST_NAME);
        email = user.get(sessionManager.EMAIL);
        user_id = user.get(sessionManager.ID);
    }

    @NonNull
    @Override
    public MedicineHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.medicine_search_row, parent, false);


        return new MedicineHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MedicineHolder holder, int position) {

        final MedicineList currentData = list.get(position);

        final String medicine_id = currentData.getMedicine_id();
        final String medicine_name = currentData.getMedicine_name();
        final String medicine_desc = currentData.getMedicine_short_desc();
        final String medicine_price = currentData.getMedicine_regular_price();
        final String medicine_price_sale = currentData.getMedicine_sale_price();

        final String medicine_image = currentData.getMedicine_image();

        //   category_name = category_name.replace("&#8211;", "-");


        List<String> colors;

        colors = new ArrayList<String>();

        colors.add("#f2453d");
        colors.add("#fd9727");
        colors.add("#fdc02f");
        colors.add("#50ae54");
        colors.add("#2c98f0");
        colors.add("#4054b2");
        colors.add("#9a30ae");
        colors.add("#e72564");

        Random r = new Random();
        int i1 = r.nextInt(7 - 0) + 0;

        GradientDrawable draw = new GradientDrawable();
        draw.setShape(GradientDrawable.RECTANGLE);
        draw.setColor(Color.parseColor(colors.get(i1)));

        // holder.medicine_image_view.setBackground(draw); //textview

        Picasso.get()
                .load(new GlobalUrlApi().getNewHomeUrl() + "uploads/" + medicine_image)
                // .placeholder(context.getResources().getDrawable(R.drawable.ic))
                .into(holder.medicine_image_view, new com.squareup.picasso.Callback() {
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

        holder.medicine_name_view.setText(medicine_name);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && medicine_desc != null && !medicine_desc.equals("null") && !medicine_desc.equals("")) {
            holder.medicine_short_desc.setText(Html.fromHtml(medicine_desc, Html.FROM_HTML_MODE_COMPACT));

        } else {
            if (medicine_desc != null && !medicine_desc.equals("null") && !medicine_desc.equals(""))
                holder.medicine_short_desc.setText(Html.fromHtml(medicine_desc));

        }
        if (medicine_price_sale != null && !medicine_price_sale.equals("null") && !medicine_price_sale.equals("")) {
            holder.medicine_price_view.setText("$" + medicine_price_sale + ".00");

        } else
            holder.medicine_price_view.setText("$" + medicine_price + ".00");

        holder.medicine_name_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MedDetailActivity.class);
                intent.putExtra("selectedMed", currentData);
                context.startActivity(intent);
            }
        });

        holder.medicine_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MedDetailActivity.class);
                intent.putExtra("selectedMed", currentData);
                context.startActivity(intent);
            }
        });

        holder.medicine_add_cart_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!checkAdded) {

                    if (medicine_price_sale != null && !medicine_price_sale.equals("null") && !medicine_price_sale.equals("")) {
                        mydb.insertCartItem(user_id, medicine_id, medicine_name, "1", medicine_price_sale, "0", "medicine", medicine_image);

                    } else
                        mydb.insertCartItem(user_id, medicine_id, medicine_name, "1", medicine_price, "0", "medicine", medicine_image);

                    holder.medicine_add_cart_view.setBackgroundResource(R.color.skybluedark);
                    holder.medicine_add_cart_view.setText("Added");
                    checkAdded = true;



                } else {
                    holder.medicine_add_cart_view.setBackgroundResource(R.color.Black50);
                    holder.medicine_add_cart_view.setText("Add");

                    checkAdded = false;
                    mydb.removeItemId(Integer.parseInt(medicine_id));

                }

                // ViewDialog alert = new ViewDialog();
                // alert.showDialog(context, "Product Added in Cart");
                // if (context instanceof UpdateCartInterface) {
                //     ((UpdateCartInterface) context).UpdateCart();
                // }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class MedicineHolder extends RecyclerView.ViewHolder {

        TextView medicine_name_view;
        TextView medicine_short_desc;
        TextView medicine_price_view;
        ImageView medicine_image_view;
        TextView medicine_add_cart_view;


        public MedicineHolder(@NonNull View itemView) {
            super(itemView);

            medicine_name_view = itemView.findViewById(R.id.medicine_name_view);
            medicine_short_desc = itemView.findViewById(R.id.medicine_short_desc);
            medicine_price_view = itemView.findViewById(R.id.medicine_price_view);
            medicine_image_view = itemView.findViewById(R.id.medicine_image_view);
            medicine_add_cart_view = itemView.findViewById(R.id.addCartButton);


        }
    }
}
