package com.med.medservice.Models.PatientAppointments;

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
import com.med.medservice.R;
import com.med.medservice.Utils.CartDBHelper;

import java.util.ArrayList;

public class AppointmentAdapter extends RecyclerView.Adapter<AppointmentAdapter.AppointmentHolder> {


    ArrayList<AppointmentList> list;
    Context context;
    View view;

    CartDBHelper mydb;


    public AppointmentAdapter() {
    }

    public AppointmentAdapter(ArrayList<AppointmentList> list, Context context) {
        this.list = list;
        this.context = context;
        mydb = new CartDBHelper(context);

    }

    @NonNull
    @Override
    public AppointmentHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.pat_appointment_row, parent, false);


        return new AppointmentHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final AppointmentHolder holder, final int position) {

        final AppointmentList currentData = list.get(position);



        //String image = currentData.getImage();
        String docName = currentData.getDoctor_name();
        String timing = "Timing: "+currentData.getTime()+",  Date: "+currentData.getDate();
        String status = currentData.getStatus();



        holder.docNameView.setText(docName);
        holder.docTimeDateView.setText(timing);
        holder.docStatusView.setText(status);

        holder.docAppoitmentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AppointmentDetailActivity.class);
                intent.putExtra("selectedAppointment", currentData);
                context.startActivity(intent);
            }
        });


    }


    @Override
    public int getItemCount() {
        return list.size();
    }


    public class AppointmentHolder extends RecyclerView.ViewHolder {

        TextView docNameView;
        TextView docTimeDateView;
        TextView docStatusView;
        TextView docAppoitmentView;
        TextView docAppointmentCancelView;
        ImageView docImageView;



        public AppointmentHolder(@NonNull View itemView) {
            super(itemView);

            docNameView = itemView.findViewById(R.id.docNameView);
            docTimeDateView = itemView.findViewById(R.id.docTimeDateView);
            docStatusView = itemView.findViewById(R.id.docStatusView);
            docAppoitmentView = itemView.findViewById(R.id.docAppoitmentView);
            docImageView = itemView.findViewById(R.id.docImageView);
            docAppointmentCancelView = itemView.findViewById(R.id.docAppointmentCancelView);


        }
    }
}
