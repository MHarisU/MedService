package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.med.medservice.Models.SearchPharmacies.PharmaciesList;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.Utils.SearchBillingZip;
import com.med.medservice.Utils.SearchLabDialogCheck;
import com.med.medservice.Utils.ViewDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CheckoutActivity extends AppCompatActivity implements OnMapReadyCallback {

    LinearLayout payment_done_layout;
    EditText date_expiry;


    String[] stateNames;
    String[] cityNames;
    Spinner statesSpinner,statesSpinner2, citySpinner,citySpinner2, spinner_time;

    ArrayList<String> slotArray;
    String[] timeSlots;


    SwitchCompat switchButton, switchButton2;

    ArrayList<PharmaciesList> pharmaciesList;
    RecyclerView pharmaciesRecycler;
    EditText searchEditView;

    View ChildView;
    int GetItemPosition;

    int queryCount = 0;

    CardView ShippingAddressSwitchCard, MedicinesSwitchCard, ShippingAddressCard, PharmacyZipCard, LabZipCard;

    CartDBHelper mydb;


    GoogleMap googleMap;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_checkout);

        SetupCardSwitches();

        SetupStatesSpinner();

        SetupStatesSpinner2();

        SetupSwitchButton();

        SetupSwitchButton2();

        mydb = new CartDBHelper(this);
        if (mydb.numberOfRowsMedicines() >0) {
            MedicinesSwitchCard.setVisibility(View.VISIBLE);
            PharmacyZipCard.setVisibility(View.VISIBLE);

        }else {
            MedicinesSwitchCard.setVisibility(View.GONE);
            PharmacyZipCard.setVisibility(View.GONE);

        }

        if (mydb.numberOfRowsLabs() >0) {

            LabZipCard.setVisibility(View.VISIBLE);

        }else {
            LabZipCard.setVisibility(View.GONE);
        }

        try {
            SetupTimeSlots();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        payment_done_layout = findViewById(R.id.payment_done_layout);

        date_expiry = findViewById(R.id.date_expiry);
        date_expiry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                int length = date_expiry.getText().length();

              //  Pattern p = Pattern.compile("\\d{2}-\\d{2}");  // use a better name, though


                if (length == 4) {


                    String temp = date_expiry.getText().toString();
                    String[] result = temp.split("(?!^)");
                    if (!result[2].equals("/")) {
                        date_expiry.setText(result[0] + result[1] + "/" + result[2] + result[3]);
                    }else {
                        date_expiry.setText("");
                    }
                }
            }
        });


        LoadMap();
    }

    private void LoadMap() {

        SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        supportMapFragment.getMapAsync(this);
    }

    private void SetupCardSwitches() {

        MedicinesSwitchCard = findViewById(R.id.MedicinesSwitchCard);
        ShippingAddressSwitchCard = findViewById(R.id.ShippingAddressSwitchCard);
        ShippingAddressCard = findViewById(R.id.ShippingAddressCard);
        LabZipCard = findViewById(R.id.LabZipCard);
        PharmacyZipCard = findViewById(R.id.PharmacyZipCard);


    }

    private void SetupTimeSlots() throws ParseException {

        String format = "yyyy-dd-MM HH:mm:SS";

        SimpleDateFormat sdf = new SimpleDateFormat(format);

        Date dateObj1 = sdf.parse("2021-01-01" + " " + "10:00:00");
        Date dateObj2 = sdf.parse("2021-01-01" + " " + "20:00:00");
        // Date dateObj1 = sdf.parse( slotStartTime);
        //  Date dateObj2 = sdf.parse( slotEndTime);
        System.out.println("Date Start: " + dateObj1);
        System.out.println("Date End: " + dateObj2);

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
            SetupTimeSpinner();
        } catch (NullPointerException e) {
            ViewDialog viewDialog = new ViewDialog();
            viewDialog.showDialog(CheckoutActivity.this, "No timeslots available on this date");
        }

    }


    private void SetupSwitchButton() {
        final TextView medicineDeliveryText = findViewById(R.id.medicineDeliveryText);
        switchButton = findViewById(R.id.switchButton);
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {

                    ShippingAddressSwitchCard.setVisibility(View.VISIBLE);

                    medicineDeliveryText.setText("Yes");
                    medicineDeliveryText.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bg));


                } else {

                    ShippingAddressSwitchCard.setVisibility(View.GONE);
                    ShippingAddressCard.setVisibility(View.GONE);
                    switchButton2.setChecked(true);

                    medicineDeliveryText.setText("No");
                    medicineDeliveryText.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_bg));

                }

            }
        });

    }

    private void SetupSwitchButton2() {
        final TextView sameDeliveryText = findViewById(R.id.sameDeliveryText);
        switchButton2 = findViewById(R.id.switchButton2);
        switchButton2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {

                if (b) {

                    ShippingAddressCard.setVisibility(View.GONE);


                    sameDeliveryText.setText("Yes");
                    sameDeliveryText.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bg));

                } else {

                    ShippingAddressCard.setVisibility(View.VISIBLE);

                    sameDeliveryText.setText("No");
                    sameDeliveryText.setBackgroundDrawable(getResources().getDrawable(R.drawable.red_bg));

                }

            }
        });

    }

    private void SetupTimeSpinner() {

        spinner_time = findViewById(R.id.spinner_time);


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
                //selectedTime = timeSlots[positions];
                //Toast.makeText(BookAppointmentActivity.this, "" + timeSlots[positions], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }


    private void SetupStatesSpinner() {
        statesSpinner = findViewById(R.id.spinner_state);

        try {

            JSONArray parent = new JSONArray(loadStatesJSONFromAsset());
            stateNames = new String[parent.length()+1];

            stateNames[0] = "Select Your State";
            for (int i = 0; i < parent.length(); i++) {


                JSONObject child = parent.getJSONObject(i);
                stateNames[i+1] = child.getString("name");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, stateNames);

        statesSpinner.setAdapter(adapter);

        statesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int positions = statesSpinner.getSelectedItemPosition();
                //   Toast.makeText(PrefenceActivity.this, "" + itemsID[positions], Toast.LENGTH_SHORT).show();
                // courseSpinnerID = courseID[positions];
                // if (positions != 0)
                //Toast.makeText(BookAppointmentActivity.this, "" + courseName[positions], Toast.LENGTH_SHORT).show();
                if (stateNames[positions].equals("Select Your State")) {
                    //Toast.makeText(CheckoutActivity.this, "Select a state please", Toast.LENGTH_SHORT).show();
                } else {
                   // selected_state = stateNames[positions];
                    Toast.makeText(CheckoutActivity.this, "" + stateNames[positions], Toast.LENGTH_SHORT).show();

                    SetupCitySpinner(stateNames[positions]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }


    private void SetupStatesSpinner2() {
        statesSpinner2 = findViewById(R.id.spinner_state2);

        try {

            JSONArray parent = new JSONArray(loadStatesJSONFromAsset());
            stateNames = new String[parent.length()+1];

            stateNames[0] = "Select Your State";
            for (int i = 0; i < parent.length(); i++) {


                JSONObject child = parent.getJSONObject(i);
                stateNames[i+1] = child.getString("name");

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, stateNames);

        statesSpinner2.setAdapter(adapter);

        statesSpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int positions = statesSpinner2.getSelectedItemPosition();
                //   Toast.makeText(PrefenceActivity.this, "" + itemsID[positions], Toast.LENGTH_SHORT).show();
                // courseSpinnerID = courseID[positions];
                // if (positions != 0)
                //Toast.makeText(BookAppointmentActivity.this, "" + courseName[positions], Toast.LENGTH_SHORT).show();
                if (stateNames[positions].equals("Select Your State")) {
                  //  Toast.makeText(CheckoutActivity.this, "Select a state please", Toast.LENGTH_SHORT).show();
                } else {
                    // selected_state = stateNames[positions];
                    Toast.makeText(CheckoutActivity.this, "" + stateNames[positions], Toast.LENGTH_SHORT).show();

                    SetupCitySpinner2(stateNames[positions]);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void SetupCitySpinner(String stateName) {

        citySpinner = findViewById(R.id.spinner_city);

        try {

            JSONObject parent = new JSONObject(loadCityJSONFromAsset());
            JSONArray city = parent.getJSONArray(stateName);
            cityNames = new String[city.length()+1];

            cityNames[0] = "Select Your City";
            for (int i = 0; i < city.length(); i++) {


                //JSONObject child = city.getJSONObject(i);
                cityNames[i+1] = city.getString(i);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }




        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, cityNames);

        citySpinner.setAdapter(adapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int positions = citySpinner.getSelectedItemPosition();
                //   Toast.makeText(PrefenceActivity.this, "" + itemsID[positions], Toast.LENGTH_SHORT).show();
                // courseSpinnerID = courseID[positions];
                // if (positions != 0)
                //Toast.makeText(BookAppointmentActivity.this, "" + courseName[positions], Toast.LENGTH_SHORT).show();
                if (cityNames[positions].equals("Select Your City")) {
                    Toast.makeText(CheckoutActivity.this, "Select a city please", Toast.LENGTH_SHORT).show();
                } else {
                    // selected_state = stateNames[positions];
                    Toast.makeText(CheckoutActivity.this, "" + cityNames[positions], Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

    }

    private void SetupCitySpinner2(String stateName) {

        citySpinner2 = findViewById(R.id.spinner_city2);

        try {

            JSONObject parent = new JSONObject(loadCityJSONFromAsset());
            JSONArray city = parent.getJSONArray(stateName);
            cityNames = new String[city.length()+1];

            cityNames[0] = "Select Your City";
            for (int i = 0; i < city.length(); i++) {


                //JSONObject child = city.getJSONObject(i);
                cityNames[i+1] = city.getString(i);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }




        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.spinner_item, cityNames);

        citySpinner2.setAdapter(adapter);

        citySpinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                int positions = citySpinner2.getSelectedItemPosition();
                //   Toast.makeText(PrefenceActivity.this, "" + itemsID[positions], Toast.LENGTH_SHORT).show();
                // courseSpinnerID = courseID[positions];
                // if (positions != 0)
                //Toast.makeText(BookAppointmentActivity.this, "" + courseName[positions], Toast.LENGTH_SHORT).show();
                if (cityNames[positions].equals("Select Your City")) {
                    Toast.makeText(CheckoutActivity.this, "Select a city please", Toast.LENGTH_SHORT).show();
                } else {
                    // selected_state = stateNames[positions];
                    Toast.makeText(CheckoutActivity.this, "" + cityNames[positions], Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

    }


    public String loadCityJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("us_cities.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public String loadStatesJSONFromAsset() {
        String json = null;
        try {
            InputStream is = getAssets().open("states_json.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


    public void Close(View view) {
        finish();
    }

    public void ConfirmOrder(View view) {
        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);


        payment_done_layout.setVisibility(View.VISIBLE);
        payment_done_layout.startAnimation(slide_up);
        EmptyCart();

        //august_month_check = true;

    }

    private void EmptyCart() {
        CartDBHelper mydb;
        mydb = new CartDBHelper(this);
        mydb.removeAllItems(0);

    }


    @Override
    public void onBackPressed() {
        finish();
    }

    public void OpenPharmacy(View view) {
        Intent intent = new Intent(this, OrderHistoryActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    public void OpenHome(View view) {
        Intent i = new Intent(this, PatientMainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }

    public void SelectDateTime(View view) {
        final TextView date_time = findViewById(R.id.date_time);

        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");

               // selected_date_of_birht = simpleDateFormat.format(calendar.getTime());

                date_time.setText("Date of Birth: " + simpleDateFormat.format(calendar.getTime()));


            }
        };

        new DatePickerDialog(CheckoutActivity.this, AlertDialog.THEME_HOLO_LIGHT, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();


    }

    public void setLab(String selected_pharmacy_id, String selected_pharmacy_name, String selected_pharmacy_address) {
        TextView labSelectedNameView = findViewById(R.id.labSelectedNameView);

        labSelectedNameView.setText(selected_pharmacy_name +", "+selected_pharmacy_address);

    }

    public void setBillingCityState(String zip ,String city, String state, String abb) {
        TextView billingAddressZip = findViewById(R.id.billingAddressZip);
        TextView billingCity = findViewById(R.id.billingCity);
        TextView billingState = findViewById(R.id.billingState);

        billingAddressZip.setText(zip);
        billingCity.setText(city);
        billingState.setText(state);

    }

    public void setMed(String selected_pharmacy_id, String selected_pharmacy_name, String selected_pharmacy_address) {
        TextView pharmacySelectedNameView = findViewById(R.id.pharmacySelectedNameView);

        pharmacySelectedNameView.setText(selected_pharmacy_name +", "+selected_pharmacy_address);

    }

    public void SearchZip(View view) {

        SearchLabDialogCheck searchLabDialog = new SearchLabDialogCheck();
        searchLabDialog.showSearchLabsDialog(CheckoutActivity.this, "lab");
    }

    public void SearchBillingZip(View view) {

        SearchBillingZip searchLabDialog = new SearchBillingZip();
        searchLabDialog.showSearchLabsDialog(CheckoutActivity.this, "bill");
    }

    public void SearchZip2(View view) {

        SearchLabDialogCheck searchLabDialog = new SearchLabDialogCheck();
        searchLabDialog.showSearchLabsDialog(CheckoutActivity.this, "med");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        this.googleMap = googleMap;

    }

    public void setPharmacyMarker(String name, String lat, String long_){

        LinearLayout pharmacyMapLayout = findViewById(R.id.pharmacyMapLayout);
        pharmacyMapLayout.setVisibility(View.VISIBLE);

        LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(long_));
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.title(name);
        markerOptions.position(latLng);
        googleMap.addMarker(markerOptions);
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        googleMap.animateCamera(cameraUpdate);

    }


}