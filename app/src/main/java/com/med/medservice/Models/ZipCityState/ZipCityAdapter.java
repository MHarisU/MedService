package com.med.medservice.Models.ZipCityState;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.med.medservice.R;

import java.util.ArrayList;

public class ZipCityAdapter extends RecyclerView.Adapter<ZipCityAdapter.ZipCityHolder> {



    ArrayList<ZipCityList> list;
    Context context;
    View view;


    public ZipCityAdapter(ArrayList<ZipCityList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ZipCityHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.pharmacies_row, parent, false);


        return new ZipCityHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ZipCityHolder holder, int position) {
        ZipCityList currentData = list.get(position);

        String city = currentData.getCity();
        String state = currentData.getState();
        String abb = currentData.getAbb();

        holder.cityNameView.setText(city);
        holder.stateNameView.setText(state + ", "+abb);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ZipCityHolder extends RecyclerView.ViewHolder {

        TextView cityNameView;
        TextView stateNameView;


        public ZipCityHolder(@NonNull View itemView) {
            super(itemView);

            cityNameView = itemView.findViewById(R.id.pharmacyName);
            stateNameView = itemView.findViewById(R.id.pharmacyAddress);


        }
    }
}
