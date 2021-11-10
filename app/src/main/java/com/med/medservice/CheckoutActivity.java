package com.med.medservice;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;
import com.med.medservice.Diaglogs.ViewDialogRegistration;
import com.med.medservice.Models.CartItems.CartItemsList;
import com.med.medservice.Models.SearchPharmacies.PharmaciesList;
import com.med.medservice.Utils.CartDBHelper;
import com.med.medservice.NetworkAPI.GlobalUrlApi;
import com.med.medservice.Diaglogs.SearchBillingZip;
import com.med.medservice.Diaglogs.SearchLabDialogCheck;
import com.med.medservice.Utils.SessionManager;
import com.med.medservice.Diaglogs.ViewDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CheckoutActivity extends AppCompatActivity implements OnMapReadyCallback {

    LinearLayout payment_done_layout;
    EditText date_expiry;


    String[] stateNames;
    String[] cityNames;
    Spinner statesSpinner, statesSpinner2, citySpinner, citySpinner2, spinner_time;

    ArrayList<String> slotArray;
    String[] timeSlots;


    SwitchCompat switchButton, switchButton2;
    Boolean deliveryCheck = false;
    Boolean sameShippingCheck = true;

    ArrayList<PharmaciesList> pharmaciesList;
    RecyclerView pharmaciesRecycler;
    EditText searchEditView;

    View ChildView;
    int GetItemPosition;

    int queryCount = 0;

    CardView ShippingAddressSwitchCard, MedicinesSwitchCard, ShippingAddressCard, PharmacyZipCard, LabZipCard;

    CartDBHelper mydb;
    ArrayList<CartItemsList> cartItemsLists;


    GoogleMap googleMap;

    ProgressDialog progressDialog;

    String total = "0";


    String first_name, last_name, user_id, email, phone;
    SessionManager sessionManager;

    EditText firstName, middleName, lastName, cardNumber, cvcNumber, phoneNumber, emailAddress, billingAddressFull;
    TextView expiryDate;
    TextView billingAddressZip;
    TextView billingCity;
    TextView billingState;

    TextView lab_appointment_date_time;
    String selected_lab_appointment_date;
    TextView pharmacySelectedNameView;
    TextView labSelectedNameView;

    String appointment_time;

    EditText shippingFullName, shippingPhone, shippingEmail, shippingAddress, shippingZip;

    String success_order_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_checkout);

        Intent intent = getIntent();

        total = intent.getStringExtra("price");
        //Toast.makeText(this, "" + total, Toast.LENGTH_SHORT).show();

        getUser();

        SetupUI();

        SetupCardSwitches();

        //SetupStatesSpinner();

        SetupStatesSpinner2();

        SetupSwitchButton();

        SetupSwitchButton2();

        mydb = new CartDBHelper(this);
        cartItemsLists = mydb.getAllItems();
        if (mydb.numberOfRowsMedicines() > 0) {
            MedicinesSwitchCard.setVisibility(View.VISIBLE);
            PharmacyZipCard.setVisibility(View.VISIBLE);

        } else {
            MedicinesSwitchCard.setVisibility(View.GONE);
            PharmacyZipCard.setVisibility(View.GONE);

        }

        if (mydb.numberOfRowsLabs() > 0) {

            LabZipCard.setVisibility(View.VISIBLE);

        } else {
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
                    } else {
                        date_expiry.setText("");
                    }
                }
            }
        });


        LoadMap();
    }

    private void SetupUI() {

        firstName = findViewById(R.id.firstName);
        middleName = findViewById(R.id.middleName);
        lastName = findViewById(R.id.lastName);
        cardNumber = findViewById(R.id.cardNumber);
        cvcNumber = findViewById(R.id.cvcNumber);
        phoneNumber = findViewById(R.id.phoneNumber);
        emailAddress = findViewById(R.id.emailAddress);
        billingAddressFull = findViewById(R.id.billingAddressFull);
        expiryDate = findViewById(R.id.expiryDate);
        billingAddressZip = findViewById(R.id.billingAddressZip);
        billingCity = findViewById(R.id.billingCity);
        billingState = findViewById(R.id.billingState);

        firstName.setText(first_name);
        lastName.setText(last_name);
        phoneNumber.setText(phone);
        emailAddress.setText(email);


        shippingFullName = findViewById(R.id.shippingFullName);
        shippingPhone = findViewById(R.id.shippingPhone);
        shippingEmail = findViewById(R.id.shippingEmail);
        shippingAddress = findViewById(R.id.shippingAddress);
        shippingZip = findViewById(R.id.shippingZip);


    }

    private void getUser() {
        sessionManager = new SessionManager(this);
        sessionManager.checkLogin();

        HashMap<String, String> user = sessionManager.getUserDetail();
        first_name = user.get(sessionManager.FIRST_NAME);
        last_name = user.get(sessionManager.LAST_NAME);
        email = user.get(sessionManager.EMAIL);
        phone = user.get(sessionManager.PHONE);
        user_id = user.get(sessionManager.ID);
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
                    deliveryCheck = true;
                    medicineDeliveryText.setText("Yes");
                    medicineDeliveryText.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bg));


                } else {

                    ShippingAddressSwitchCard.setVisibility(View.GONE);
                    ShippingAddressCard.setVisibility(View.GONE);
                    switchButton2.setChecked(true);
                    deliveryCheck = false;
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
                    sameShippingCheck = true;

                    sameDeliveryText.setText("Yes");
                    sameDeliveryText.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_bg));

                } else {

                    ShippingAddressCard.setVisibility(View.VISIBLE);
                    sameShippingCheck = false;

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
                appointment_time = timeSlots[position];
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
            stateNames = new String[parent.length() + 1];

            stateNames[0] = "Select Your State";
            for (int i = 0; i < parent.length(); i++) {


                JSONObject child = parent.getJSONObject(i);
                stateNames[i + 1] = child.getString("name");

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
            stateNames = new String[parent.length() + 1];

            stateNames[0] = "Select Your State";
            for (int i = 0; i < parent.length(); i++) {


                JSONObject child = parent.getJSONObject(i);
                stateNames[i + 1] = child.getString("name");

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
            cityNames = new String[city.length() + 1];

            cityNames[0] = "Select Your City";
            for (int i = 0; i < city.length(); i++) {


                //JSONObject child = city.getJSONObject(i);
                cityNames[i + 1] = city.getString(i);

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
            cityNames = new String[city.length() + 1];

            cityNames[0] = "Select Your City";
            for (int i = 0; i < city.length(); i++) {


                //JSONObject child = city.getJSONObject(i);
                cityNames[i + 1] = city.getString(i);

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

        /*progressDialog = new ProgressDialog(CheckoutActivity.this);
        progressDialog.setTitle("Please wait");
        progressDialog.setCancelable(false);
        progressDialog.show();*/
        CreateOrder();


        //august_month_check = true;

    }

    private void CreateOrder() {


        Boolean checkOrderSequence = true;

        //Billing Section Params
        String payment_card_number = cardNumber.getText().toString();
        String payment_card_expiry = expiryDate.getText().toString();
        String payment_card_cvc = cvcNumber.getText().toString();

        String billing_address_zip = billingAddressZip.getText().toString();
        String billing_city = billingCity.getText().toString();
        String billing_state = billingState.getText().toString();
        String billing_address_full = billingAddressFull.getText().toString();
        String billing_phone_number = phoneNumber.getText().toString();
        String billing_email = emailAddress.getText().toString();


        String errors = "";

        // Validating Billing Section
        if (!ValidateBillingSection(payment_card_number, payment_card_expiry, payment_card_cvc,
                billing_address_zip, billing_city, billing_state, billing_address_full)) {



            errors += "* Please fill all fields (in Billing Section)\n\n";
            ViewDialogRegistration viewDialog = new ViewDialogRegistration();
            viewDialog.showDialog(CheckoutActivity.this, errors);
            checkOrderSequence = false;
        } else {


            //Shipping Section
            String shipping_full_name = "";
            String shipping_phone = "";
            String shipping_email = "";
            String shipping_address = "";
            String shipping_state = "";
            String shipping_city = "";
            String shipping_zip = "";

            String deliver = "off";
            if (deliveryCheck) {
                deliver = "on";

                if (sameShippingCheck) {
                    shipping_full_name = first_name + " " + last_name;
                    shipping_phone = billing_phone_number;
                    shipping_email = billing_email;
                    shipping_address = billing_address_full;
                    shipping_state = billing_state;
                    shipping_city = billing_city;
                    shipping_zip = billing_address_zip;
                } else {
                    shipping_full_name = shippingFullName.getText().toString();
                    shipping_phone = shippingPhone.getText().toString();
                    shipping_email = shippingEmail.getText().toString();
                    shipping_address = shippingAddress.getText().toString();
                    shipping_state = billing_state;
                    shipping_city = billing_city;
                    shipping_zip = shippingZip.getText().toString();
                }


            } else {
                deliver = "off";
            }

            if (deliveryCheck) {

                if (!sameShippingCheck) {

                    if (!ValidateShippingSection(shipping_full_name, shipping_phone, shipping_email, shipping_address, shipping_state, shipping_city, shipping_zip)) {
                        errors += "* Please fill all fields (in Shipping Section)\n\n";
                        ViewDialogRegistration viewDialog = new ViewDialogRegistration();
                        viewDialog.showDialog(CheckoutActivity.this, errors);
                        checkOrderSequence = false;
                    }

                }

            }


            String pharmacy_zip_name = "";
            if (mydb.numberOfRowsMedicines() > 0) {
                try {
                    pharmacy_zip_name = pharmacySelectedNameView.getText().toString();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            String lab_zip_name = "";
            if (mydb.numberOfRowsLabs() > 0) {
                lab_zip_name = labSelectedNameView.getText().toString();
            }

            if (checkOrderSequence){

                progressDialog = new ProgressDialog(CheckoutActivity.this);
                progressDialog.setTitle("Please wait");
                progressDialog.setCancelable(false);
                progressDialog.show();


                JSONObject orderJsonObject = new JSONObject();
                try {
                    orderJsonObject.put("customer_id", user_id);
                    orderJsonObject.put("currency", "US");
                    orderJsonObject.put("order_status", "processing");
                    orderJsonObject.put("payment_title", "Direct Bank Transfer");
                    orderJsonObject.put("payment_method", "via PayPal");
                    orderJsonObject.put("total_price", total);

                    JSONObject billingObject = new JSONObject();


                    billingObject.put("first_name", first_name);
                    billingObject.put("middle_name", "");
                    billingObject.put("last_name", last_name);
                    billingObject.put("address", billing_address_full);
                    billingObject.put("state", billing_state);
                    billingObject.put("state_code", "AK");
                    billingObject.put("city", billing_city);
                    billingObject.put("zip_code", billing_address_zip);
                    billingObject.put("phone_number", billing_phone_number);
                    billingObject.put("email_address", billing_email);
                    billingObject.put("medicines_delivery", deliver);
                    billingObject.put("pharmacy_zipcode", pharmacy_zip_name);
                    billingObject.put("pharmacy_nearby_location", "404");
                    billingObject.put("lab_appointment_date", selected_lab_appointment_date);
                    billingObject.put("lab_appointment_time", appointment_time);
                    billingObject.put("lab_zipcode", lab_zip_name);
                    billingObject.put("lab_nearby_location", "410");

                    orderJsonObject.put("billing", billingObject);

                    JSONObject shippingObject = new JSONObject();

                    shippingObject.put("full_name", shipping_full_name);
                    shippingObject.put("email_address", shipping_email);
                    shippingObject.put("phone_number", shipping_phone);
                    shippingObject.put("address", shipping_address);
                    shippingObject.put("state", shipping_state);
                    shippingObject.put("state_code", "FL");
                    shippingObject.put("zip_code", shipping_zip);

                    orderJsonObject.put("shipping", shippingObject);

                    JSONObject paymentObject = new JSONObject();

                    paymentObject.put("card_number", payment_card_number);
                    paymentObject.put("card_expiry", payment_card_expiry);
                    paymentObject.put("cvc", payment_card_cvc);
                    paymentObject.put("payment_method", "online_bank_transfer");
                    paymentObject.put("payment_method_title", "Direct Bank Transfer / Online Payment");
                    paymentObject.put("transaction_id", "4AV75196BA3587207");
                    paymentObject.put("payment_status", "Paid");

                    orderJsonObject.put("payment", paymentObject);

                    JSONArray cartItemsArray = new JSONArray();
                    for (int i = 0; i < cartItemsLists.size(); i++) {
                        JSONObject cartItem1 = new JSONObject();
                        CartItemsList items = cartItemsLists.get(i);
                        cartItem1.put("product_id", items.getITEM_ID());
                        cartItem1.put("product_qty", items.getQUANTITY());

                        cartItem1.put("pres_id", "111");
                        cartItem1.put("doc_session_id", "144");
                        cartItem1.put("product_mode", items.getTYPE());
                        cartItem1.put("item_type", "session");

                        cartItemsArray.put(cartItem1);


                    }
            /*JSONObject cartItem1 = new JSONObject();
            cartItem1.put("product_id", "41");
            cartItem1.put("product_qty", "2");
            cartItem1.put("pres_id", "111");
            cartItem1.put("doc_session_id", "144");
            cartItem1.put("product_mode", "medicine");
            cartItem1.put("item_type", "session");

            cartItemsArray.put(cartItem1);


            JSONObject cartItem2 = new JSONObject();
            cartItem2.put("product_id", "92");
            cartItem2.put("product_qty", "3");
            cartItem2.put("pres_id", "111");
            cartItem2.put("doc_session_id", "144");
            cartItem2.put("product_mode", "lab-test");
            cartItem2.put("item_type", "db");

            cartItemsArray.put(cartItem2);*/

                    orderJsonObject.put("cart_items", cartItemsArray);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


                final String requestBody = orderJsonObject.toString();
                // Toast.makeText(this, ""+requestBody, Toast.LENGTH_SHORT).show();

                Log.d("createOrder", requestBody);

                StringRequest stringRequest = new StringRequest(Request.Method.POST, new GlobalUrlApi().getNewBaseUrl() + "createOrder",
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                Log.d("order_api_response", response);
                                Log.d("createOrder", response);


                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONObject jsonResponse = jsonObject.getJSONObject("Response");
                                    JSONObject jsonData = jsonResponse.getJSONObject("Data");
                                    String jsonStatus = jsonResponse.getString("Status");

                                    if (jsonStatus.equals("True")) {


                                        String OrderID = jsonData.getString("OrderID").trim();
                                        String Message = jsonData.getString("Message").trim();

                                        progressDialog.dismiss();
                                        Toast.makeText(CheckoutActivity.this, "" + OrderID, Toast.LENGTH_LONG).show();

                                        Animation slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                                                R.anim.slide_down);

                                        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                                                R.anim.slide_up);

                                        TextView orderCompleteMessageView = findViewById(R.id.orderCompleteMessageView);
                                        orderCompleteMessageView.setText("Thank you\nOrder Successfully Placed\nYour ORDER ID is " + OrderID);

                                        payment_done_layout.setVisibility(View.VISIBLE);
                                        payment_done_layout.startAnimation(slide_up);
                                        EmptyCart();


                                    } else {

                                        progressDialog.dismiss();
                                        AlertDialog.Builder dialog = new AlertDialog.Builder(CheckoutActivity.this, R.style.DialogTheme)
                                                .setTitle("Warning!")
                                                .setMessage("Order not created")
                                                .setCancelable(false)
                                                .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialogInterface, int i) {


                                                    }
                                                });
                                        //      dialog.show().getWindow().setBackgroundDrawableResource(R.drawable.backgroud_alertbox_round);
                                        dialog.show();


                                    }


                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Log.d("createOrder", requestBody);

                                    //   login_button.setVisibility(View.VISIBLE);
                                    //   progress_bar.setVisibility(View.GONE);
                                    // Toast.makeText(LoginActivity.this, "Error "+e.toString(), Toast.LENGTH_SHORT).show();
                                    Toast.makeText(CheckoutActivity.this, "Json Error.", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                    //  login_text.setVisibility(View.VISIBLE);
                                    // login_text.setText("JSON Error");
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                //  login_button.setVisibility(View.VISIBLE);
                                //  progress_bar.setVisibility(View.GONE);
                                //  Toast.makeText(LoginActivity.this, "Error "+error.toString(), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(LoginActivity.this, ""+error.toString(), Toast.LENGTH_SHORT).show();
                                //Login(email, password);
                                //    login_text.setVisibility(View.VISIBLE);
                                //   login_text.setText("Error from php");

                                //  login_button.setVisibility(View.VISIBLE);
                                // progress_bar.setVisibility(View.GONE);
                                Log.d("createOrder", requestBody);

                                error.printStackTrace();
                                progressDialog.dismiss();
                                AlertDialog.Builder dialog = new AlertDialog.Builder(CheckoutActivity.this, R.style.DialogTheme)
                                        .setTitle("Warning!")
                                        .setMessage("Volley Error")
                                        .setCancelable(false)
                                        .setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                //  login_button.setVisibility(View.VISIBLE);
                                                //  progress_bar.setVisibility(View.GONE);

                                            }
                                        });
                                //      dialog.show().getWindow().setBackgroundDrawableResource(R.drawable.backgroud_alertbox_round);
                                dialog.show();

                            }
                        }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }

                    @Override
                    public byte[] getBody() throws AuthFailureError {
                        try {
                            return requestBody == null ? null : requestBody.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                            return null;
                        }
                    }

                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<String, String>();
                        String auth = "Bearer " + new SessionManager(CheckoutActivity.this).getToken();
                        headers.put("Authorization", auth);
                        return headers;
                    }
                };

                stringRequest.setRetryPolicy(new RetryPolicy() {
                    @Override
                    public int getCurrentTimeout() {
                        return 10000;
                    }

                    @Override
                    public int getCurrentRetryCount() {
                        return 0; //retry turn off
                    }

                    @Override
                    public void retry(VolleyError error) throws VolleyError {

                    }
                });

                RequestQueue requestQueue = Volley.newRequestQueue(this);
                requestQueue.add(stringRequest);

            }


        }


    }

    private boolean ValidateShippingSection(String shipping_full_name, String shipping_phone, String shipping_email, String shipping_address,
                                            String shipping_state, String shipping_city, String shipping_zip) {

        return shipping_full_name != null && !shipping_full_name.equals("")
                && shipping_phone != null && !shipping_phone.equals("")
                && shipping_email != null && !shipping_email.equals("")
                && shipping_address != null && !shipping_address.equals("")
                && shipping_state != null && !shipping_state.equals("")
                && shipping_city != null && !shipping_city.equals("")
                && shipping_zip != null && !shipping_zip.equals("");


    }

    private boolean ValidateBillingSection(String payment_card_number, String payment_card_expiry, String payment_card_cvc,
                                           String billing_address_zip, String billing_city, String billing_state, String billing_address_full) {

        return payment_card_number.length() == 16
                && validateCardExpiryDate(payment_card_expiry)
                && payment_card_cvc.length() == 3
                && billing_address_zip != null && !billing_address_zip.equals("")
                && billing_city != null && !billing_city.equals("")
                && billing_state != null && !billing_state.equals("")
                && billing_address_full != null && !billing_address_full.equals("");

    }

    boolean validateCardExpiryDate(String expiryDate) {
        return expiryDate.matches("(?:0[1-9]|1[0-2])/[0-9]{2}");
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
        Intent intent = new Intent(this, PatientMainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(intent);
    }

    public void OpenHome(View view) {
        SessionManager sessionManager = new SessionManager(this);

        Intent i = null;

        if (sessionManager.getUserType().equals("patient")) {
            i = new Intent(this, PatientMainActivity.class);
        } else if (sessionManager.getUserType().equals("doctor")) {
            i = new Intent(this, DoctorMainActivity.class);
        }
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        finish();
        startActivity(i);
    }

    public void SelectDateTime(View view) {
        lab_appointment_date_time = findViewById(R.id.date_time);

        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy");
                SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("dd/MM/yyyy");

                // selected_date_of_birht = simpleDateFormat.format(calendar.getTime());

                lab_appointment_date_time.setText("Date of Birth: " + simpleDateFormat.format(calendar.getTime()));
                selected_lab_appointment_date = "" + simpleDateFormat2.format(calendar.getTime());


            }
        };

        new DatePickerDialog(CheckoutActivity.this, AlertDialog.THEME_HOLO_LIGHT, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();


    }

    public void setLab(String selected_pharmacy_id, String selected_pharmacy_name, String selected_pharmacy_address) {
        labSelectedNameView = findViewById(R.id.labSelectedNameView);

        labSelectedNameView.setText(selected_pharmacy_name + ", " + selected_pharmacy_address);

    }

    public void setBillingCityState(String zip, String city, String state, String abb) {
        TextView billingAddressZip = findViewById(R.id.billingAddressZip);
        TextView billingCity = findViewById(R.id.billingCity);
        TextView billingState = findViewById(R.id.billingState);

        billingAddressZip.setText(zip);
        billingCity.setText(city);
        billingState.setText(state);

    }

    public void setMed(String selected_pharmacy_id, String selected_pharmacy_name, String selected_pharmacy_address) {
        pharmacySelectedNameView = findViewById(R.id.pharmacySelectedNameView);

        pharmacySelectedNameView.setText(selected_pharmacy_name + ", " + selected_pharmacy_address);

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

    public void setPharmacyMarker(String name, String lat, String long_) {

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


    public void selectExpiry(View view) {
        // createDialogWithoutDateField().show();
        RackMonthPicker picker = new RackMonthPicker(this);

        picker
                .setLocale(Locale.ENGLISH)
                .setColorTheme(Color.BLUE)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {


                        year = year - 2000;
                        if (month < 10)
                            expiryDate.setText("0" + month + "/" + year);
                        else
                            expiryDate.setText(month + "/" + year);

                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {

                    @Override
                    public void onCancel(androidx.appcompat.app.AlertDialog dialog) {

                    }
                }).show();
    }

    private DatePickerDialog createDialogWithoutDateField() {
        DatePickerDialog dpd = new DatePickerDialog(this, null, 2014, 1, 24);
        try {
            java.lang.reflect.Field[] datePickerDialogFields = dpd.getClass().getDeclaredFields();
            for (java.lang.reflect.Field datePickerDialogField : datePickerDialogFields) {
                if (datePickerDialogField.getName().equals("mDatePicker")) {
                    datePickerDialogField.setAccessible(true);
                    DatePicker datePicker = (DatePicker) datePickerDialogField.get(dpd);
                    java.lang.reflect.Field[] datePickerFields = datePickerDialogField.getType().getDeclaredFields();
                    for (java.lang.reflect.Field datePickerField : datePickerFields) {
                        Log.i("test", datePickerField.getName());
                        if ("mDaySpinner".equals(datePickerField.getName())) {
                            datePickerField.setAccessible(true);
                            Object dayPicker = datePickerField.get(datePicker);
                            ((View) dayPicker).setVisibility(View.GONE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
        }
        return dpd;
    }
}