package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.applandeo.materialcalendarview.CalendarView;
import com.applandeo.materialcalendarview.EventDay;
import com.applandeo.materialcalendarview.exceptions.OutOfDateRangeException;
import com.applandeo.materialcalendarview.listeners.OnDayClickListener;
import com.med.medservice.Models.DoctorAppointments.DocAppointmentAdapter;
import com.med.medservice.Models.PatientAppointments.AppointmentAdapter;
import com.med.medservice.Models.PatientAppointments.AppointmentList;
import com.med.medservice.Models.ProductLabs.LabHorizAdapter;
import com.med.medservice.Models.ProductLabs.LabsList;
import com.med.medservice.Utils.ApiCallerNew;
import com.med.medservice.Utils.GlobalUrlApi;
import com.med.medservice.Utils.SessionManager;
import com.med.medservice.Utils.ViewDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DoctorCalendarScheduleActivity extends AppCompatActivity {

    List<EventDay> events;
    CalendarView calendarView;


    String user_id;
    SessionManager sessionManager;

    String clickedDate = null;
    String clickedStartTime = null;
    String clickedEndTime = null;


    Spinner doctorSpinner, spinner_time, monthSpinner;
    String[] timeSlots;
    ArrayList<String> slotArray;
    String selectedTime;

    ArrayList<String> availDates;



    RecyclerView appointmentRecycler;
    ArrayList<AppointmentList> appointmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_doctor_calendar_schedule);

        sessionManager = new SessionManager(this);
        HashMap<String, String> user = sessionManager.getUserDetail();
        user_id = user.get(sessionManager.ID);

        events = new ArrayList<>();
        calendarView = (CalendarView) findViewById(R.id.calendarView);


        Calendar calendar = Calendar.getInstance();
        //  calendar.set(1996, 9, 8);
        try {
            calendarView.setDate(calendar);
        } catch (OutOfDateRangeException e) {
            e.printStackTrace();
        }

        //  events.add(new EventDay(calendar, R.drawable.ic_event));
        calendarView.setEvents(events);

        GetDoctorAvailabilities();

        calendarView.setOnDayClickListener(new OnDayClickListener() {
            @Override
            public void onDayClick(EventDay eventDay) {
                Calendar clickedDayCalendar = eventDay.getCalendar();
                boolean added = false;
                for (int i = 0; i < events.size(); i++) {
                    if (eventDay == events.get(i)) {
                        added = true;
                        break;
                    }
                }
                if (added) {
                    //   Toast.makeText(DoctorCalendarScheduleActivity.this, "Already", Toast.LENGTH_SHORT).show();
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-dd-MM");

                    clickedDate = simpleDateFormat.format(clickedDayCalendar.getTime());
                   // UpdateDialog alert = new UpdateDialog();
                  //  alert.updateDialog(DoctorCalendarScheduleActivity.this, simpleDateFormat.format(clickedDayCalendar.getTime()), clickedDayCalendar);
                    Intent intent = new Intent(DoctorCalendarScheduleActivity.this, DoctorUpdateAvailabilityActivity.class);
                    intent.putExtra("date", clickedDate);
                    startActivity(intent);

                } else {
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-dd-MM");

                    clickedDate = simpleDateFormat.format(clickedDayCalendar.getTime());
                    //  SetupTimeSpinner();
                    SetDialog alert = new SetDialog();
                    alert.showDialog(DoctorCalendarScheduleActivity.this, simpleDateFormat.format(clickedDayCalendar.getTime()), clickedDayCalendar);


                }

            }
        });

        GetAppointments();
    }

    private void GetAppointments() {

        appointmentRecycler = null;
        appointmentList = null;

        appointmentRecycler = findViewById(R.id.appoitmentsRecycler);
        // noticeRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        appointmentRecycler.setLayoutManager(new LinearLayoutManager(this));
        appointmentList = new ArrayList<AppointmentList>();

        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_doctor_upcoming_appointment.php?doctor_id="+user_id,
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            try {

                                //  Log.d("ApiResponse", response);

                                try {

                                    JSONArray parent = new JSONArray(response);

                                    for (int i = 0; i < parent.length(); i++) {
                                        JSONObject child = parent.getJSONObject(i);
                                        String id = child.getString("id");
                                        String patient_id = child.getString("patient_id");
                                        String doctor_id = child.getString("doctor_id");
                                        String patient_name = child.getString("patient_name");
                                        String doctor_name = child.getString("doctor_name");
                                        String email = child.getString("email");
                                        String phone = child.getString("phone");
                                        String date = child.getString("date");
                                        String time = child.getString("time");
                                        String problem = child.getString("problem");
                                        String status = child.getString("status");
                                        String day = child.getString("day");

                                        appointmentList.add(new AppointmentList(id, patient_id, doctor_id, patient_name, doctor_name, email, phone,
                                                date, time, problem, status,day));


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            } catch (NullPointerException e) {
                                e.printStackTrace();
                            }


                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }


                        DocAppointmentAdapter adapter = new DocAppointmentAdapter(appointmentList, DoctorCalendarScheduleActivity.this);
                        appointmentRecycler.setAdapter(adapter);


                        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.slide_up);
                        // cardProgress.setVisibility(View.GONE);
                        //  layoutMain.setVisibility(View.VISIBLE);
                        //  layoutMain.startAnimation(slide_up);

                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    private void GetDoctorAvailabilities() {


        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "get_doctor_schedules.php?doctor_id=" + user_id,
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            // Log.d("Availabilities", response);

                            try {

                                JSONArray parent = new JSONArray(response);
                                // doctorNames = new String[parent.length()];
                                // doctorId = new String[parent.length()];

                                // doctorNames[0] = "Select Doctor";
                                //  doctorId[0] = "";

                                availDates = new ArrayList<>();
                                for (int i = 0; i < parent.length(); i++) {
                                    JSONObject child = parent.getJSONObject(i);
                                    // String id = child.getString("id");
                                    String slotStartTime = child.getString("slotStartTime");
                                    String slotEndTime = child.getString("slotEndTime");
                                    String datee = child.getString("date");

                                    availDates.add(datee);

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }

                        //   doctorNames = new String[5];
                        //   doctorNames[0] = "Select Doctor";
                        //   doctorNames[1] = "Dr. Jane Edward";
                        //  doctorNames[2] = "Dr. Saleem Javed";
                        //   doctorNames[3] = "Dr. Imran Ali";
                        //  doctorNames[4] = "Dr. Aftab Khan";

                        // SetupDoctorSpinner();

                        try {
                            for (int i = 0; i < availDates.size(); i++) {

                                Calendar calendar = Calendar.getInstance();

                                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-dd-MM");
                                calendar.setTime(simpleDateFormat.parse(availDates.get(i)));// all done
                                //calendar.set(1996, 9, 8);
                                events.add(new EventDay(calendar, R.drawable.ic_event));
                                calendarView.setEvents(events);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                       // Toast.makeText(DoctorCalendarScheduleActivity.this, "" + availDates.get(0), Toast.LENGTH_LONG).show();


                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void SetupTimeSpinner(final Spinner spinner_time) {


        String slotStartTime = "01:00:00";
        String slotEndTime = "24:00:00";

        String format = "yyyy-dd-MM HH:mm:SS";

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Date dateObj1 = null;
        Date dateObj2 = null;
        try {
            dateObj1 = sdf.parse(clickedDate + " " + slotStartTime);
            dateObj2 = sdf.parse(clickedDate + " " + slotEndTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        slotArray = new ArrayList<>();
        long dif = dateObj1.getTime();
        while (dif <= dateObj2.getTime()) {
            Date slot = new Date(dif);

            // SimpleDateFormat form = new SimpleDateFormat("HH:mm:ss");
            SimpleDateFormat form = new SimpleDateFormat("hh:mm a");
            slotArray.add("" + form.format(slot));

            System.out.println("Minute Slot ---> " + form.format(slot) + " SIZE:" + slotArray.size());
            dif += 1800000;
        }

        try {

            timeSlots = new String[slotArray.size()];

            for (int jj = 0; jj < slotArray.size(); jj++) {
                timeSlots[jj] = slotArray.get(jj);
            }
        } catch (NullPointerException e) {
            ViewDialog viewDialog = new ViewDialog();
            //  viewDialog.showDialog(BookAppointmentActivity.this, "No timeslots available on this date");
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, timeSlots);


        spinner_time.setAdapter(adapter);

        spinner_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int positions = spinner_time.getSelectedItemPosition();
                //   Toast.makeText(PrefenceActivity.this, "" + itemsID[positions], Toast.LENGTH_SHORT).show();
                // courseSpinnerID = courseID[positions];
                //  if (positions != 0)
                //selectedDoctorId = timeSlots[positions];
                selectedTime = timeSlots[positions];
                SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm:SS");
                SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
                try {
                    Date date = parseFormat.parse(selectedTime);
                    //Toast.makeText(DoctorCalendarScheduleActivity.this, "" + displayFormat.format(date), Toast.LENGTH_SHORT).show();
                    clickedEndTime = "" + displayFormat.format(date);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });


    }


    public void Close(View view) {
        finish();
    }


    public class SetDialog {

        public void showDialog(final Context activity, String msg, final Calendar clickedDayCalendar) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_set_availability_layout);

            Spinner spinner_time = (Spinner) dialog.findViewById(R.id.spinner_time);

            SetupTimeSpinner(spinner_time);

            TextView selectedDate = (TextView) dialog.findViewById(R.id.selectedDate);
            selectedDate.setText(msg);

            ImageView closeDialog = (ImageView) dialog.findViewById(R.id.closeDialog);
            closeDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            final TextView from_time = (TextView) dialog.findViewById(R.id.from_time);
            from_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelectStartTime(from_time);
                }
            });

            final TextView to_time = (TextView) dialog.findViewById(R.id.to_time);
            to_time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SelectEndTime(to_time);
                }
            });


            TextView setAvailability = (TextView) dialog.findViewById(R.id.setAvailability);
            setAvailability.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //   events.add(new EventDay(clickedDayCalendar, R.drawable.ic_event));
                    //   calendarView.setEvents(events);
                    //   dialog.dismiss();

                    if (clickedDate != null && clickedStartTime != null && clickedEndTime != null) {

                        SetAvailability(dialog, clickedDayCalendar);

                    } else {
                        Toast.makeText(activity, "Select your timiing please...", Toast.LENGTH_SHORT).show();
                    }

                }
            });

            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        }
    }

    private void SetAvailability(final Dialog dialog, final Calendar clickedDayCalendar) {
        ApiCallerNew asyncTask = new ApiCallerNew(new GlobalUrlApi().getBaseUrl() + "set_doctor_availability.php?" +
                "doctor_id=" + user_id +
                "&start_time=" + clickedStartTime +
                "&end_time=" + clickedEndTime +
                "&datee=" + clickedDate,
                new ApiCallerNew.AsyncApiResponse() {

                    @Override
                    public void processFinish(String response) {
                        try {
                            Log.d("ApiResponse", response);

                            try {

                                JSONObject child = new JSONObject(response);
                                String success = child.getString("success");
                                if (success.equals("1")) {

                                    events.add(new EventDay(clickedDayCalendar, R.drawable.ic_event));
                                    calendarView.setEvents(events);
                                    dialog.dismiss();

                                    final AlertDialog.Builder aDialog = new AlertDialog.Builder(DoctorCalendarScheduleActivity.this, R.style.DialogTheme)
                                            .setTitle("")
                                            .setMessage("Availability Added")
                                            .setCancelable(false)
                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                    aDialog.show();

                                } else {
                                    final AlertDialog.Builder aDialog = new AlertDialog.Builder(DoctorCalendarScheduleActivity.this, R.style.DialogTheme)
                                            .setTitle("")
                                            .setMessage("Availability Not Added")
                                            .setCancelable(false)
                                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                }
                                            });
                                    aDialog.show();
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } catch (Exception e) {
                            Log.d("EXCEPTION", e.toString());

                        }


                    }

                });

        // asyncTask.execute();
        asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }

    public void SelectStartTime(final TextView from_time) {
        final TextView date_time = findViewById(R.id.date_time);

        final Calendar calendar = Calendar.getInstance();


        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minutes);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy | ");

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
                from_time.setText(aTime);

                String newHours = "" + hour;
                if (newHours.length() > 1) {
                } else {
                    newHours = "0" + hour;
                }

                String newMinutes = "" + minutes;
                if (newMinutes.length() > 1) {
                } else {
                    newMinutes = "0" + minutes;
                }

                clickedStartTime = "" + newHours + ":" + newMinutes + ":00";
                //  Toast.makeText(DoctorCalendarScheduleActivity.this, ""+hour+":"+minutes+":00", Toast.LENGTH_LONG).show();


            }
        };

        new TimePickerDialog(DoctorCalendarScheduleActivity.this, android.R.style.Theme_Holo_Light_Dialog, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();


    }

    public void SelectEndTime(final TextView to_time) {
        final TextView date_time = findViewById(R.id.date_time);

        final Calendar calendar = Calendar.getInstance();


        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minutes);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy | ");

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
                to_time.setText(aTime);

                // clickedEndTime = "" + hour + ":" + minutes + ":00";


                String newHours = "" + hour;
                if (newHours.length() > 1) {
                } else {
                    newHours = "0" + hour;
                }

                String newMinutes = "" + minutes;
                if (newMinutes.length() > 1) {
                } else {
                    newMinutes = "0" + minutes;
                }

                clickedEndTime = "" + newHours + ":" + newMinutes + ":00";
                //  Toast.makeText(DoctorCalendarScheduleActivity.this, ""+hour+":"+minutes+":00", Toast.LENGTH_LONG).show();


            }
        };

        new TimePickerDialog(DoctorCalendarScheduleActivity.this, android.R.style.Theme_Holo_Light_Dialog, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show();


    }


    public class UpdateDialog {

        public void updateDialog(final Context activity, String msg, final Calendar clickedDayCalendar) {
            final Dialog dialog = new Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_update_availability_layout);

            TextView selectedDate = (TextView) dialog.findViewById(R.id.selectedDate);

            final LinearLayout appointmentLayout = (LinearLayout) dialog.findViewById(R.id.appointmentLayout);
            final LinearLayout editLayout = (LinearLayout) dialog.findViewById(R.id.editLayout);


            final TextView appointmentButton = (TextView) dialog.findViewById(R.id.appointmentButton);

            final TextView editButton = (TextView) dialog.findViewById(R.id.editButton);
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editButton.setBackgroundColor(Color.parseColor("#2c97ff"));
                    appointmentButton.setBackgroundColor(Color.parseColor("#1A3C3C3C"));

                    editButton.setTextColor(Color.parseColor("#FFFFFF"));
                    appointmentButton.setTextColor(Color.parseColor("#232323"));

                    editLayout.setVisibility(View.VISIBLE);
                    appointmentLayout.setVisibility(View.GONE);

                }
            });

            appointmentButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    editButton.setBackgroundColor(Color.parseColor("#1A3C3C3C"));
                    appointmentButton.setBackgroundColor(Color.parseColor("#2c97ff"));

                    editButton.setTextColor(Color.parseColor("#232323"));
                    appointmentButton.setTextColor(Color.parseColor("#FFFFFF"));

                    editLayout.setVisibility(View.GONE);
                    appointmentLayout.setVisibility(View.VISIBLE);

                }
            });

            selectedDate.setText(msg);

            ImageView closeDialog = (ImageView) dialog.findViewById(R.id.closeDialog);
            closeDialog.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            TextView setAvailability = (TextView) dialog.findViewById(R.id.setAvailability);
            setAvailability.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    events.add(new EventDay(clickedDayCalendar, R.drawable.ic_event));
                    calendarView.setEvents(events);
                    dialog.dismiss();

                    final AlertDialog.Builder aDialog = new AlertDialog.Builder(activity, R.style.DialogTheme)
                            .setTitle("")
                            .setMessage("Availability added")
                            .setCancelable(false)
                            .setNegativeButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });
                    aDialog.show();

                }
            });

            dialog.show();
            Window window = dialog.getWindow();
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        }
    }

}