package com.med.medservice.Models.PanelLabs;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.Models.Category.CategoryList;
import com.med.medservice.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PanelLabsAdapter extends RecyclerView.Adapter<PanelLabsAdapter.LabsHolder> {


    ArrayList<PanelLabsList> list;
    Context context;
    View view;

    public PanelLabsAdapter() {
    }

    public PanelLabsAdapter(ArrayList<PanelLabsList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public LabsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.panel_lab_row, parent, false);


        return new LabsHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LabsHolder holder, int position) {

        final PanelLabsList currentData = list.get(position);

        String lab_name = currentData.getLab_name();
        String lab_price = currentData.getLab_price();
        String lab_cpt = currentData.getLab_cpt();


        holder.labName.setText(lab_name);
        holder.labPrice.setText(lab_price);
        holder.labCpt.setText(lab_cpt);


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class LabsHolder extends RecyclerView.ViewHolder {

        TextView labName;
        TextView labPrice;
        TextView labCpt;


        public LabsHolder(@NonNull View itemView) {
            super(itemView);

            labName = itemView.findViewById(R.id.labName);
            labPrice = itemView.findViewById(R.id.labPrice);
            labCpt = itemView.findViewById(R.id.labCpt);


        }
    }
}
