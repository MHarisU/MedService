package com.med.medservice.Models.CartItems;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.CartActivity;
import com.med.medservice.Models.Category.CategoryList;
import com.med.medservice.R;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.Utils.ViewDialog;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartHolder> {


    ArrayList<CartItemsList> list;
    Context context;
    View view;

    CartDBHelper mydb;


    public CartAdapter() {
    }

    public CartAdapter(ArrayList<CartItemsList> list, Context context) {
        this.list = list;
        this.context = context;
        mydb = new CartDBHelper(context);

    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.cart_row, parent, false);


        return new CartHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartHolder holder, final int position) {

        final CartItemsList currentData = list.get(position);

        final String id = currentData.getID();
        String user_id = currentData.getUSER_ID();
        String item_id = currentData.getITEM_ID();
        String name = currentData.getNAME();
        final String quantity = currentData.getQUANTITY();
        String price = currentData.getPRICE();
        String discount = currentData.getDISCOUNT();
        String type = currentData.getTYPE();
        String image = currentData.getIMAGE();


        holder.cartTitleView.setText(name);
        holder.cartPriceView.setText("$" + price + ".00");
        holder.cartQuantityView.setText(quantity);

        if (type.equals("medicine")) {
            Picasso.get()
                    .load(image)
                    // .placeholder(context.getResources().getDrawable(R.drawable.ic))
                    .into(holder.cartImageView, new com.squareup.picasso.Callback() {
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
        }
        else if (type.equals("lab-test")){

            holder.cartImageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.test_lab));

            holder.quantityLayout.setVisibility(View.GONE);
            holder.tagText.setText("Lab");
            holder.tagText.setBackgroundResource(R.color.purplesharp);

        }


        holder.cartRemoveItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ViewDialog alert = new ViewDialog();
                alert.showDialog(context, "Product Removed From Cart");

                removeItemFromView(position);
                mydb.deleteItem(Integer.parseInt(id));

                if (context instanceof CartActivity) {
                    ((CartActivity) context).CalculateTotal(list);
                }
            }
        });

        holder.cartAddQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer tempQuantity = Integer.parseInt(holder.cartQuantityView.getText().toString());
                tempQuantity++;
                mydb.updateQuantity(Integer.parseInt(id), String.valueOf(tempQuantity));
                holder.cartQuantityView.setText("" + tempQuantity);

                currentData.setQUANTITY("" + tempQuantity);
                notifyDataSetChanged();
                notifyItemChanged(position);

                if (context instanceof CartActivity) {
                    ((CartActivity) context).CalculateTotal(list);
                }
            }
        });

        holder.cartRemoveQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer tempQuantity = Integer.parseInt(holder.cartQuantityView.getText().toString());
                if (tempQuantity > 1) {
                    tempQuantity--;
                    mydb.updateQuantity(Integer.parseInt(id), String.valueOf(tempQuantity));
                    holder.cartQuantityView.setText("" + tempQuantity);

                    currentData.setQUANTITY("" + tempQuantity);
                    notifyDataSetChanged();
                    notifyItemChanged(position);

                    if (context instanceof CartActivity) {
                        ((CartActivity) context).CalculateTotal(list);
                    }
                }
            }
        });


    }

    public void removeItemFromView(int position) {
        try {
            this.list.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, list.size());
            //removes the row
        } catch (Exception e) {
            notifyDataSetChanged();
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class CartHolder extends RecyclerView.ViewHolder {

        TextView cartTitleView;
        TextView cartPriceView;
        TextView cartRemoveItem;
        TextView cartQuantityView;
        ImageView cartImageView;
        ImageView cartAddQuantity;
        ImageView cartRemoveQuantity;
        LinearLayout quantityLayout;
        TextView tagText;


        public CartHolder(@NonNull View itemView) {
            super(itemView);

            cartTitleView = itemView.findViewById(R.id.cartTitleView);
            cartPriceView = itemView.findViewById(R.id.cartPriceView);
            cartRemoveItem = itemView.findViewById(R.id.cartRemoveItem);
            cartQuantityView = itemView.findViewById(R.id.cartQuantityView);
            cartImageView = itemView.findViewById(R.id.cartImageView);
            cartAddQuantity = itemView.findViewById(R.id.cartAddQuantity);
            cartRemoveQuantity = itemView.findViewById(R.id.cartRemoveQuantity);
            quantityLayout = itemView.findViewById(R.id.quantityLayout);
            tagText = itemView.findViewById(R.id.tagText);


        }
    }
}
