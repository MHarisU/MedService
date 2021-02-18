package com.med.medservice.Models.SearchPharmacies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.Models.SessionsVideoCall.SessionsAdapter;
import com.med.medservice.Models.SessionsVideoCall.SessionsList;
import com.med.medservice.R;

import java.util.ArrayList;

public class PharmaciesAdapter extends RecyclerView.Adapter<PharmaciesAdapter.PharmaciesHolder> {



    ArrayList<PharmaciesList> list;
    Context context;
    View view;


    public PharmaciesAdapter(ArrayList<PharmaciesList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public PharmaciesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.pharmacies_row, parent, false);


        return new PharmaciesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PharmaciesHolder holder, int position) {
        PharmaciesList currentData = list.get(position);

        String name = currentData.getPharmacy_name();
        String address = currentData.getPharmacy_address();

        holder.pharmacyName.setText(name);
        holder.pharmacyAddress.setText(address);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class PharmaciesHolder extends RecyclerView.ViewHolder {

        TextView pharmacyName;
        TextView pharmacyAddress;


        public PharmaciesHolder(@NonNull View itemView) {
            super(itemView);

            pharmacyName = itemView.findViewById(R.id.pharmacyName);
            pharmacyAddress = itemView.findViewById(R.id.pharmacyAddress);


        }
    }
}
