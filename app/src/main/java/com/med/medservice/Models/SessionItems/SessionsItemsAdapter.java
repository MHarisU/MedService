package com.med.medservice.Models.SessionItems;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.Models.SessionsPatient.SessionsList;
import com.med.medservice.R;
import com.med.medservice.SessionDetailActivity;

import java.util.ArrayList;

public class SessionsItemsAdapter extends RecyclerView.Adapter<SessionsItemsAdapter.ItemsHolder> {

    ArrayList<SessionsItemsList> list;
    Context context;
    View view;

    public SessionsItemsAdapter() {
    }

    public SessionsItemsAdapter(ArrayList<SessionsItemsList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ItemsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.session_items_row, parent, false);


        return new ItemsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsHolder holder, int position) {

        final SessionsItemsList currentData = list.get(position);

        String product_name = currentData.getProduct_name();
        String quantity = currentData.getQuantity();
        String dosage = currentData.getUsage();

        holder.productName.setText(product_name);
        holder.quantityView.setText(quantity == "null"?"2":quantity);
        holder.dosageView.setText(dosage == "null"?"Dosage: empty":dosage);




    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class ItemsHolder extends RecyclerView.ViewHolder {

        TextView productName;
        TextView quantityView;
        TextView dosageView;


        public ItemsHolder(@NonNull View itemView) {
            super(itemView);

            productName = itemView.findViewById(R.id.productName);
            quantityView = itemView.findViewById(R.id.quantityView);
            dosageView = itemView.findViewById(R.id.dosageView);


        }
    }

}
