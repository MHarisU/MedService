package com.med.medservice.Models.OnlineDoctors;

import java.io.Serializable;

public class OnlineDoctorsList implements Serializable {

    String doctor_id, doctor_name, doctor_email, doctor_specialization;


    public String getDoctor_id() {
        return doctor_id;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getDoctor_email() {
        return doctor_email;
    }

    public String getDoctor_specialization() {
        return doctor_specialization;
    }

    public OnlineDoctorsList() {
    }

    public OnlineDoctorsList(String doctor_id, String doctor_name, String doctor_email, String doctor_specialization) {
        this.doctor_id = doctor_id;
        this.doctor_name = doctor_name;
        this.doctor_email = doctor_email;
        this.doctor_specialization = doctor_specialization;
    }
}
