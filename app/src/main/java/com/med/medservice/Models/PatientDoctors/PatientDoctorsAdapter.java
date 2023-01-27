package com.med.medservice.Models.PatientDoctors;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.med.medservice.NewUI.PatientDoctorsListNewActivity;
import com.med.medservice.R;

import java.util.ArrayList;

public class PatientDoctorsAdapter extends RecyclerView.Adapter<PatientDoctorsAdapter.ViewHolder> {

    private ArrayList<PatientDoctorsList> DoctorsList;
    PatientDoctorsListNewActivity context;
    private String url;

    public PatientDoctorsAdapter(PatientDoctorsListNewActivity context, ArrayList<PatientDoctorsList> DoctorsList) {
        this.DoctorsList = DoctorsList;
        this.context = context;

    }



        @NonNull
    @Override
    public PatientDoctorsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View view = inflater.inflate(R.layout.patient_doctors_list_row, parent, false);
            return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull PatientDoctorsAdapter.ViewHolder holder, int position) {
        PatientDoctorsList doctorsList = DoctorsList.get(position);
        holder.first_name.setText(doctorsList.getFirstName());
        holder.last_name.setText(doctorsList.getLastName());
        holder.specialization.setText(doctorsList.getSpecialization());









    }

    @Override
    public int getItemCount() {
        return DoctorsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView specialization;
        TextView first_name;
        TextView last_name;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            first_name = itemView.findViewById(R.id.docFirstName);
            last_name = itemView.findViewById(R.id.docLastName);
            specialization = itemView.findViewById(R.id.specialization);

        }
    }
}
