package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DoctorScheduleActivity extends AppCompatActivity {

    CheckBox monCheck, tueCheck, wedCheck, thurCheck, friCheck;

    TextView editButton, from_time, to_time;

    boolean editcheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_schedule);

        initializeUI();
    }

    private void initializeUI() {
        monCheck = findViewById(R.id.monCheck);
        tueCheck = findViewById(R.id.tueCheck);
        wedCheck = findViewById(R.id.wedCheck);
        thurCheck = findViewById(R.id.thurCheck);
        friCheck = findViewById(R.id.friCheck);

        editButton = findViewById(R.id.editButton);
        from_time = findViewById(R.id.from_time);
        to_time = findViewById(R.id.to_time);

    }

    public void Close(View view) {
        finish();
    }

    public void OpenCalendar(View view) {
        startActivity(new Intent(this, DoctorCalendarScheduleActivity.class));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public void EditButton(View view) {
        if (!editcheck) {
            monCheck.setClickable(true);
            tueCheck.setClickable(true);
            wedCheck.setClickable(true);
            thurCheck.setClickable(true);
            friCheck.setClickable(true);

            from_time.setClickable(true);
            to_time.setClickable(true);

            editcheck = true;

            editButton.setText("SAVE");
            editButton.setBackground(getResources().getDrawable(R.drawable.green_bg));
        } else {

            monCheck.setClickable(false);
            tueCheck.setClickable(false);
            wedCheck.setClickable(false);
            thurCheck.setClickable(false);
            friCheck.setClickable(false);

            from_time.setClickable(false);
            to_time.setClickable(false);

            editcheck = false;

            editButton.setText("EDIT SCHEDULE");
            editButton.setBackground(getResources().getDrawable(R.drawable.blue_background_button));


        }
    }

    public void FromTime(View view) {

        if (editcheck) {

            final TextView from_time = findViewById(R.id.from_time);
            final Calendar calendar = Calendar.getInstance();
            TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minutes);
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy | ");
                    String timeSet = "";
                    if (hour > 12) {
                        hour -= 12;
                        timeSet = "PM";
                    } else if (hour == 0) {
                        hour += 12;
                        timeSet = "AM";
                    } else if (hour == 12) {
                        timeSet = "PM";
                    } else {
                        timeSet = "AM";
                    }
                    String min = "";
                    if (minutes < 10)
                        min = "0" + minutes;
                    else
                        min = String.valueOf(minutes);
                    String aTime = new StringBuilder().append(hour).append(':')
                            .append(min).append(" ").append(timeSet).toString();
                    from_time.setText("" + aTime);

                }
            };
            new TimePickerDialog(DoctorScheduleActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
        }
    }

    public void ToTime(View view) {

        if (editcheck) {
            final TextView to_time = findViewById(R.id.to_time);

            final Calendar calendar = Calendar.getInstance();

            TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minutes);

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy | ");

                    //converting
                    String timeSet = "";
                    if (hour > 12) {
                        hour -= 12;
                        timeSet = "PM";
                    } else if (hour == 0) {
                        hour += 12;
                        timeSet = "AM";
                    } else if (hour == 12) {
                        timeSet = "PM";
                    } else {
                        timeSet = "AM";
                    }

                    String min = "";
                    if (minutes < 10)
                        min = "0" + minutes;
                    else
                        min = String.valueOf(minutes);

                    // Append in a StringBuilder
                    String aTime = new StringBuilder().append(hour).append(':')
                            .append(min).append(" ").append(timeSet).toString();
                    //  et1.setText(aTime);
                    to_time.setText("" + aTime);


                }
            };

            new TimePickerDialog(DoctorScheduleActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
        }
    }

    public void AddHoliday(View view) {

        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");

                Toast.makeText(DoctorScheduleActivity.this, "Holiday Added on : \n" + simpleDateFormat.format(calendar.getTime()), Toast.LENGTH_LONG).show();


            }
        };

        new DatePickerDialog(DoctorScheduleActivity.this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();

    }
}