package com.med.medservice.Models.DoctorAppointments;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.AppointmentDetailActivity;
import com.med.medservice.Models.PatientAppointments.AppointmentList;
import com.med.medservice.R;
import com.med.medservice.Utils.CartDBHelper;

import java.util.ArrayList;

public class DocAppointmentAdapter extends RecyclerView.Adapter<DocAppointmentAdapter.AppointmentHolder> {


    ArrayList<AppointmentList> list;
    Context context;
    View view;

    CartDBHelper mydb;


    public DocAppointmentAdapter() {
    }

    public DocAppointmentAdapter(ArrayList<AppointmentList> list, Context context) {
        this.list = list;
        this.context = context;
        mydb = new CartDBHelper(context);

    }

    @NonNull
    @Override
    public AppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.doc_appointment_row, parent, false);


        return new AppointmentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AppointmentHolder holder, final int position) {

        final AppointmentList currentData = list.get(position);



        //String image = currentData.getImage();
        String patNAME = currentData.getPatient_name();
        String timing = ""+currentData.getTime()+" -- "+currentData.getDate();
        String status = currentData.getStatus();



        holder.patNameView.setText("With "+patNAME);
        holder.patTimeDateView.setText("Time: "+timing);


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class AppointmentHolder extends RecyclerView.ViewHolder {

        TextView patNameView;
        TextView patTimeDateView;



        public AppointmentHolder(@NonNull View itemView) {
            super(itemView);

            patNameView = itemView.findViewById(R.id.patientName);
            patTimeDateView = itemView.findViewById(R.id.timeAndDate);


        }
    }
}
