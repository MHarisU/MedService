package com.med.medservice.NewUI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

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

import com.med.medservice.BookAppointmentActivity;
import com.med.medservice.DoctorScheduleActivity;
import com.med.medservice.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DoctorScheduleNewActivity extends AppCompatActivity {

    Dialog addScheduleDialog, deleteScheduleDialog, editScheduleDialog;


    TextView selectDate, fromTime, ToTime;

    DatePickerDialog datePickerDialog;

    TimePickerDialog timePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_schedule_new);


        setUpAddScheduleDialog();

        setUpDltScheduleDialog();

        setUpEditScheduleDialog();





    }

    private void setUpAddScheduleDialog() {

        addScheduleDialog = new Dialog(DoctorScheduleNewActivity.this);
        addScheduleDialog.setContentView(R.layout.add_schedule_doctor_new);
        addScheduleDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addScheduleDialog.setCancelable(false);

        Button addScheduleButton = findViewById(R.id.addSchedule);
        addScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addScheduleDialog.show();
            }
        });
        Button closeDialogButton = addScheduleDialog.findViewById(R.id.closeScheduleDialogButton);
        closeDialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addScheduleDialog.dismiss();
            }
        });
    }

    private void setUpDltScheduleDialog() {
        deleteScheduleDialog = new Dialog(DoctorScheduleNewActivity.this);
        deleteScheduleDialog.setContentView(R.layout.delete_schedule_dialog_new);
        deleteScheduleDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        deleteScheduleDialog.setCancelable(false);

        Button deleteScheduleButton = findViewById(R.id.dltSchedule);
        deleteScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteScheduleDialog.show();
            }
        });


    }

    private void setUpEditScheduleDialog() {
        editScheduleDialog = new Dialog(DoctorScheduleNewActivity.this);
        editScheduleDialog.setContentView(R.layout.edit_schedule_dialog_new);
        editScheduleDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editScheduleDialog.setCancelable(false);

        Button editScheduleButton = findViewById(R.id.editScheduleBtn);
        editScheduleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editScheduleDialog.show();
            }
        });


    }



    public void GoBackToMain(View view) {
        finish();

    }


    public void openDoctorNewAppointments(View view) {
        startActivity(new Intent(DoctorScheduleNewActivity.this, DoctorNewAppointmentsActivity.class));

    }

    public void closeAddSchedule(View view) {
        addScheduleDialog.dismiss();
    }


    public void openNotifications(View view) {
        startActivity(new Intent(DoctorScheduleNewActivity.this, NotificationsNewActivity.class));

    }

    public void OpenCartActivity(View view) {
        startActivity(new Intent(DoctorScheduleNewActivity.this, CartNewActivity.class));

    }

    public void closeDltDialog(View view) {
        deleteScheduleDialog.dismiss();
    }

    public void closeDltOnBtn(View view) {
        deleteScheduleDialog.dismiss();

    }

    public void closeEditSchedule(View view) {
        editScheduleDialog.dismiss();
    }

    public void closeEditOnCancel(View view) {
        editScheduleDialog.dismiss();
    }

    public void openCalendar(View view) {

        selectDate = findViewById(R.id.selectDate);
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);

        datePickerDialog = new DatePickerDialog(DoctorScheduleNewActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                selectDate.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);

        datePickerDialog.show();
    }


//    public void fromTime(View view) {
//
//        if (editcheck) {
//
//            fromTime = findViewById(R.id.fromTime);
//            final Calendar calendar = Calendar.getInstance();
//            TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
//                @Override
//                public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
//                    calendar.set(Calendar.HOUR_OF_DAY, hour);
//                    calendar.set(Calendar.MINUTE, minutes);
//                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy | ");
//                    String timeSet = "";
//                    if (hour > 12) {
//                        hour -= 12;
//                        timeSet = "PM";
//                    } else if (hour == 0) {
//                        hour += 12;
//                        timeSet = "AM";
//                    } else if (hour == 12) {
//                        timeSet = "PM";
//                    } else {
//                        timeSet = "AM";
//                    }
//                    String min = "";
//                    if (minutes < 10)
//                        min = "0" + minutes;
//                    else
//                        min = String.valueOf(minutes);
//                    String aTime = new StringBuilder().append(hour).append(':')
//                            .append(min).append(" ").append(timeSet).toString();
//                    fromTime.setText("" + aTime);
//
//                }
//            };
//            new TimePickerDialog(DoctorScheduleNewActivity.this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();
//        }
//    }

    // Add schedule time (From)
    public void fromTime(View view) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(DoctorScheduleNewActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                fromTime.setText( selectedHour + ":" + selectedMinute);
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

        timePickerDialog = new TimePickerDialog(DoctorScheduleNewActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                ToTime.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }


    // Edit schedule time (From)
    public void editFromTime(View view) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(DoctorScheduleNewActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                fromTime.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    // Edit schedule time (To)
    public void editToTime(View view) {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        timePickerDialog = new TimePickerDialog(DoctorScheduleNewActivity.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
               // ToTime.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true);//Yes 24 hour time
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }
}
