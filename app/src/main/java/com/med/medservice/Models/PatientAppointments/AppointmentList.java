package com.med.medservice.Models.PatientAppointments;

import java.io.Serializable;

public class AppointmentList  implements Serializable {

    String id, patient_id, doctor_id, patient_name, doctor_name, email_pat, phone_pat, date, time, problem, status, day;

    public String getId() {
        return id;
    }

    public String getPatient_id() {
        return patient_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public String getPatient_name() {
        return patient_name;
    }

    public String getDoctor_name() {
        return doctor_name;
    }

    public String getEmail_pat() {
        return email_pat;
    }

    public String getPhone_pat() {
        return phone_pat;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getProblem() {
        return problem;
    }

    public String getStatus() {
        return status;
    }

    public String getDay() {
        return day;
    }

    public AppointmentList() {
    }

    public AppointmentList(String id, String patient_id, String doctor_id, String patient_name, String doctor_name, String email_pat,
                           String phone_pat, String date, String time, String problem, String status, String day) {
        this.id = id;
        this.patient_id = patient_id;
        this.doctor_id = doctor_id;
        this.patient_name = patient_name;
        this.doctor_name = doctor_name;
        this.email_pat = email_pat;
        this.phone_pat = phone_pat;
        this.date = date;
        this.time = time;
        this.problem = problem;
        this.status = status;
        this.day = day;
    }
}
