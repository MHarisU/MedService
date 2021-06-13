package com.med.medservice.Models.SessionsVideoCall_OldApi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.med.medservice.R;
import com.med.medservice.SessionDetailActivity;

import java.util.ArrayList;

public class SessionsAdapter_OldApi extends RecyclerView.Adapter<SessionsAdapter_OldApi.SessionHolder> {

    ArrayList<SessionsList_OldApi> list;
    Context context;
    View view;

    public SessionsAdapter_OldApi() {
    }

    public SessionsAdapter_OldApi(ArrayList<SessionsList_OldApi> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SessionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.session_row, parent, false);


        return new SessionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SessionHolder holder, int position) {

        SessionsList_OldApi currentData = list.get(position);

        String session_id = currentData.getSession_id();
        String first_name = currentData.getDoctor_first_name();
        String last_name = currentData.getDoctor_last_name();
        String date = currentData.getSession_date();

        holder.doctorName.setText(first_name+" "+last_name);
        holder.dateView.setText(date);

        holder.sessionCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, SessionDetailActivity.class));
                Toast.makeText(context, ""+session_id, Toast.LENGTH_SHORT).show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    public class SessionHolder extends RecyclerView.ViewHolder {

        TextView dateView;
        TextView doctorName;
        CardView sessionCard;


        public SessionHolder(@NonNull View itemView) {
            super(itemView);

            dateView = itemView.findViewById(R.id.dateView);
            doctorName = itemView.findViewById(R.id.doctorName);
            sessionCard = itemView.findViewById(R.id.sessionCard);


        }
    }

}
