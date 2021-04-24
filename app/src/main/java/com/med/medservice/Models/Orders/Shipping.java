package com.med.medservice.Models.Orders;

public class Shipping {
    public String full_name;
    public String email_address;
    public String phone_number;
    public String address;
    public String state;
    public String state_code;
    public String zip_code;

    public Shipping(String full_name, String email_address, String phone_number, String address, String state, String state_code, String zip_code) {
        this.full_name = full_name;
        this.email_address = email_address;
        this.phone_number = phone_number;
        this.address = address;
        this.state = state;
        this.state_code = state_code;
        this.zip_code = zip_code;
    }
}