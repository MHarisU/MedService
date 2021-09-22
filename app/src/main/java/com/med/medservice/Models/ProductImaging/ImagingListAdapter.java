package com.med.medservice.Models.ProductImaging;

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

import com.med.medservice.ImagingDetailActivity;
import com.med.medservice.R;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.Utils.SessionManager;
import com.med.medservice.Utils.UpdateCartInterface;
import com.med.medservice.Diaglogs.ViewDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ImagingListAdapter extends RecyclerView.Adapter<ImagingListAdapter.ImagingHolder> {


    ArrayList<ImagingList> list;
    Context context;
    View view;


    CartDBHelper mydb;


    String name, user_id, email;
    SessionManager sessionManager;

    public ImagingListAdapter() {
    }

    public ImagingListAdapter(ArrayList<ImagingList> list, Context context) {
        this.list = list;
        this.context = context;

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
    public ImagingHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.imaging_list_row, parent, false);


        return new ImagingHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ImagingHolder holder, int position) {

        final ImagingList currentData = list.get(position);

        final String imaging_id = String.valueOf(currentData.getId());
        final String imaging_name = currentData.getName();
        final String imaging_desc = currentData.getShort_description();
        final String imaging_price = currentData.getSale_price();
        final String imaging_image = currentData.getFeatured_image();

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

        /*
        Picasso.get()
                .load(lab_image)
                .into(holder.lab_image_view, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                    }
                    @Override
                    public void onError(Exception e) {
                    }
                });

         */

        //holder.lab_image_view.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.flask_icon));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.lab_name_view.setText(Html.fromHtml(imaging_name, Html.FROM_HTML_MODE_COMPACT));
            holder.lab_short_desc.setText(Html.fromHtml(imaging_desc, Html.FROM_HTML_MODE_COMPACT));

        } else {

            holder.lab_name_view.setText(Html.fromHtml(imaging_name));
            holder.lab_short_desc.setText(Html.fromHtml(imaging_desc));
        }

        holder.lab_price_view.setText("$" + imaging_price + ".00");

        holder.lab_name_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sentence = holder.lab_name_view.getText().toString();
                String search  = "Panel";

                if ( sentence.toLowerCase().indexOf(search.toLowerCase()) != -1 ) {

                    System.out.println("I found the keyword");

                } else {

                    System.out.println("not found");

                }

                Intent intent = new Intent(context, ImagingDetailActivity.class);
                intent.putExtra("selectedLab", currentData);
                context.startActivity(intent);
            }
        });

        holder.lab_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String sentence = holder.lab_name_view.getText().toString();
                String search  = "Panel";

                if ( sentence.toLowerCase().indexOf(search.toLowerCase()) != -1 ) {

                    System.out.println("I found the keyword");

                } else {

                    System.out.println("not found");

                }

                Intent intent = new Intent(context, ImagingDetailActivity.class);
                intent.putExtra("selectedLab", currentData);
                context.startActivity(intent);
            }
        });

        holder.lab_add_cart_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                mydb.insertCartItem(user_id, imaging_id, imaging_name, "1", imaging_price, "0", "lab-test", imaging_image);

                /*ViewDialog alert = new ViewDialog();
                alert.showDialog(context, "Lab added in Cart");*/

                ViewDialog alert = new ViewDialog();
                alert.showDialog(context, "" + imaging_name + "\nAdded in Cart");

                if (context instanceof UpdateCartInterface) {
                    ((UpdateCartInterface) context).UpdateCart();
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ImagingHolder extends RecyclerView.ViewHolder {

        TextView lab_name_view;
        TextView lab_short_desc;
        TextView lab_price_view;
        ImageView lab_image_view;
        TextView lab_add_cart_view;


        public ImagingHolder(@NonNull View itemView) {
            super(itemView);

            lab_name_view = itemView.findViewById(R.id.medicine_name_view);
            lab_short_desc = itemView.findViewById(R.id.medicine_short_desc);
            lab_price_view = itemView.findViewById(R.id.medicine_price_view);
            lab_image_view = itemView.findViewById(R.id.medicine_image_view);
            lab_add_cart_view = itemView.findViewById(R.id.addCartButton);


        }
    }
}
