package com.med.medservice.Models.OnlineDoctors;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.EvisitActivity;
import com.med.medservice.EvisitFormActivity;
import com.med.medservice.MedDetailActivity;
import com.med.medservice.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class OnlineDoctorAdapter extends RecyclerView.Adapter<OnlineDoctorAdapter.DoctorHolder> {


    ArrayList<OnlineDoctorsList> list;
    Context context;
    View view;

    public OnlineDoctorAdapter() {
    }

    public OnlineDoctorAdapter(ArrayList<OnlineDoctorsList> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public DoctorHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.online_doctor_row, parent, false);


        return new DoctorHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorHolder holder, int position) {

        final OnlineDoctorsList currentData = list.get(position);

        String doctorName = currentData.getDoctor_name();
        String doctorSpecialization = currentData.getDoctor_specialization();

        holder.doctorNameView.setText((doctorName.replace("Dr. ", "")).replace(" ", "\n"));
        holder.doctorSpecialization.setText(doctorSpecialization);



        holder.talkToDoctorButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context, EvisitFormActivity.class);
                intent.putExtra("selectedDoctor", currentData);
                context.startActivity(intent);
                ((EvisitActivity) context).finish();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class DoctorHolder extends RecyclerView.ViewHolder {

        TextView doctorNameView;
        TextView doctorSpecialization;
        LinearLayout talkToDoctorButton;


        public DoctorHolder(@NonNull View itemView) {
            super(itemView);

            doctorNameView = itemView.findViewById(R.id.doctorNameView);
            doctorSpecialization = itemView.findViewById(R.id.doctorSpecialization);
            talkToDoctorButton = itemView.findViewById(R.id.talkToDoctorButton);


        }
    }
}
