package com.med.medservice.Models.Orders;

public class Billing {
    public String first_name;
    public String middle_name;
    public String last_name;
    public String address;
    public String state;
    public String state_code;
    public String city;
    public String zip_code;
    public String phone_number;
    public String email_address;
    public String pharmacy_zipcode;
    public String pharmacy_nearby_location;
    public String lab_appointment_date;
    public String lab_appointment_time;
    public String lab_zipcode;
    public String lab_nearby_location;

    public Billing(String first_name,  String last_name, String address, String state,
                   String state_code, String city, String zip_code, String phone_number, String email_address,
                   String pharmacy_zipcode, String pharmacy_nearby_location, String lab_appointment_date,
                   String lab_appointment_time, String lab_zipcode, String lab_nearby_location) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.address = address;
        this.state = state;
        this.state_code = state_code;
        this.city = city;
        this.zip_code = zip_code;
        this.phone_number = phone_number;
        this.email_address = email_address;
        this.pharmacy_zipcode = pharmacy_zipcode;
        this.pharmacy_nearby_location = pharmacy_nearby_location;
        this.lab_appointment_date = lab_appointment_date;
        this.lab_appointment_time = lab_appointment_time;
        this.lab_zipcode = lab_zipcode;
        this.lab_nearby_location = lab_nearby_location;
    }
}
