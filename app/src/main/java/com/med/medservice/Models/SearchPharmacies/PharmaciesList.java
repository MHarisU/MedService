package com.med.medservice.Models.SearchPharmacies;

import java.io.Serializable;

public class PharmaciesList implements Serializable {

    String pharmacy_id, pharmacy_name , pharmacy_state , pharmacy_address , pharmacy_city , pharmacy_zip_code , pharmacy_marker_type , pharmacy_marker_icon , pharmacy_lat , pharmacy_long;


    public String getPharmacy_id() {
        return pharmacy_id;
    }

    public String getPharmacy_name() {
        return pharmacy_name;
    }

    public String getPharmacy_state() {
        return pharmacy_state;
    }

    public String getPharmacy_address() {
        return pharmacy_address;
    }

    public String getPharmacy_city() {
        return pharmacy_city;
    }

    public String getPharmacy_zip_code() {
        return pharmacy_zip_code;
    }

    public String getPharmacy_marker_type() {
        return pharmacy_marker_type;
    }

    public String getPharmacy_marker_icon() {
        return pharmacy_marker_icon;
    }

    public String getPharmacy_lat() {
        return pharmacy_lat;
    }

    public String getPharmacy_long() {
        return pharmacy_long;
    }

    public PharmaciesList(String pharmacy_id, String pharmacy_name, String pharmacy_state, String pharmacy_address, String pharmacy_city,
                          String pharmacy_zip_code, String pharmacy_marker_type, String pharmacy_marker_icon, String pharmacy_lat, String pharmacy_long) {
        this.pharmacy_id = pharmacy_id;
        this.pharmacy_name = pharmacy_name;
        this.pharmacy_state = pharmacy_state;
        this.pharmacy_address = pharmacy_address;
        this.pharmacy_city = pharmacy_city;
        this.pharmacy_zip_code = pharmacy_zip_code;
        this.pharmacy_marker_type = pharmacy_marker_type;
        this.pharmacy_marker_icon = pharmacy_marker_icon;
        this.pharmacy_lat = pharmacy_lat;
        this.pharmacy_long = pharmacy_long;
    }
}
