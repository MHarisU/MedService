package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.med.medservice.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RefillRequestNewActivity extends AppCompatActivity {

    private Button refillButton, requestSessionButton, viewDetailsButton, closeDialogButton;
    Dialog refillDialog, requestSessionDialog;

    String selectedDate, selectedDay, selectedTime;

    TextView selectDate, fromTimer, toTimer;



    DatePickerDialog datePickerDialog;

    TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_refill_request_new);


        fromTimer = findViewById(R.id.addFromTime);
        toTimer = findViewById(R.id.addToTime);

        setUpRefillDialog();

        setUpRequestSessionDialog();




    }



    private void setUpRequestSessionDialog() {

        requestSessionDialog = new Dialog(RefillRequestNewActivity.this);
        requestSessionDialog.setContentView(R.layout.add_schedule_doctor_new);
        requestSessionDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        requestSessionDialog.setCancelable(false);

        requestSessionButton = findViewById(R.id.request_button);
        requestSessionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSessionDialog.show();
            }
        });

    }



    private void setUpRefillDialog() {

        refillDialog = new Dialog(RefillRequestNewActivity.this);
        refillDialog.setContentView(R.layout.refill_request_dialog);
        refillDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        refillDialog.setCancelable(false);

        refillButton = findViewById(R.id.refillBtn);
        refillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refillDialog.show();
            }
        });




    }

//    public void openCalendar(View view) {
//
//
//
//
//        final TextView selectDate = findViewById(R.id.calendarView);
//
//
//        final Calendar calendar = Calendar.getInstance();
//        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
//
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                calendar.set(Calendar.YEAR, year);
//                calendar.set(Calendar.MONTH, month);
//                calendar.set(Calendar.DAY_OF_MONTH, day);
//                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//
//                //Toast.makeText(BookAppointmentActivity.this, "" + dayOfWeek, Toast.LENGTH_SHORT).show();
//                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//
//                selectDate.setText("Date: " + simpleDateFormat.format(calendar.getTime()));
//
//                if (dayOfWeek == 1){
//                    selectedDay ="Sunday";
//                }
//                else if (dayOfWeek == 2){
//                    selectedDay ="Monday";
//                }
//                else if (dayOfWeek == 3){
//                    selectedDay ="Tuesday";
//                }
//                else if (dayOfWeek == 4){
//                    selectedDay ="Wednesday";
//                }
//                else if (dayOfWeek == 5){
//                    selectedDay ="Thursday";
//                }
//                else if (dayOfWeek == 6){
//                    selectedDay ="Friday";
//                }
//                else if (dayOfWeek == 7){
//                    selectedDay ="Saturday";
//                }
//
//                Toast.makeText(RefillRequestNewActivity.this, ""+selectedDay, Toast.LENGTH_SHORT).show();
//
//                selectedDate = simpleDateFormat.format(calendar.getTime());
//
//
//            }
//        };
//
//        new DatePickerDialog(RefillRequestNewActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
//
//
//    }



    public void openCalendar(View view) {

        selectDate = findViewById(R.id.selectDate);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        datePickerDialog = new DatePickerDialog(RefillRequestNewActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);

        datePickerDialog.show();
    }

    public void GoBackToMain(View view) {
        finish();
    }


    public void openCartActivity(View view) {
        startActivity(new Intent(RefillRequestNewActivity.this, CartNewActivity.class));

    }

    public void openNotifications(View view) {
        startActivity(new Intent(RefillRequestNewActivity.this, NotificationsNewActivity.class));

    }

    public void close(View view) {
        refillDialog.dismiss();

    }

    public void closeDialog(View view) {
        refillDialog.dismiss();
    }

    public void closeAddSchedule(View view) {
        requestSessionDialog.dismiss();
    }


    public void closeOnCancel(View view) {
        requestSessionDialog.dismiss();
    }

    // Add schedule time (From)
    public void fromTime(View view) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(RefillRequestNewActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                fromTimer.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    // Add schedule time (To)
    public void toTime(View view) {

        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(RefillRequestNewActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                toTimer.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
//    public void fromTime(View view) {
//        final Calendar myCalender = Calendar.getInstance();
//        int hour = myCalender.get(Calendar.HOUR_OF_DAY);
//        int minute = myCalender.get(Calendar.MINUTE);
//
//
//        TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                if (view.isShown()) {
//                    myCalender.set(Calendar.HOUR_OF_DAY, hourOfDay);
//                    myCalender.set(Calendar.MINUTE, minute);
//
//                }
//            }
//        };
//        TimePickerDialog timePickerDialog = new TimePickerDialog(RefillRequestNewActivity.this, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, myTimeListener, hour, minute, true);
//        timePickerDialog.setTitle("From:");
//        timePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//        timePickerDialog.show();
//    }



}