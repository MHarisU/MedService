package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.med.medservice.Utils.OpenPrescribedItems;

public class PrescriptionActivity extends AppCompatActivity {

    EditText diagnosisView, notesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription);

        SetupUI();

        OpenPrescribedItems prescribedItems = new OpenPrescribedItems(PrescriptionActivity.this, "PrescriptionActivity");

    }

    private void SetupUI() {
        diagnosisView = findViewById(R.id.diagnosisView);
        notesView = findViewById(R.id.notesView);

        Intent intent = getIntent();
        diagnosisView.setText(intent.getStringExtra("diagnosis"));
        notesView.setText(intent.getStringExtra("notes"));
    }

    public void Confirm(View view) {
    }
}