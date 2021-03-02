package com.med.medservice.Models.ZipCityState;

import java.io.Serializable;

public class ZipCityList implements Serializable {

    String zip_code, city, state, abb;


    public String getZip_code() {
        return zip_code;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getAbb() {
        return abb;
    }

    public ZipCityList(String zip_code, String city, String state, String abb) {
        this.zip_code = zip_code;
        this.city = city;
        this.state = state;
        this.abb = abb;
    }
}
