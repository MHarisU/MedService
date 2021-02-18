package com.med.medservice.Models.SessionsVideoCall;

import java.io.Serializable;

public class SessionsList implements Serializable {

    String session_id, doctor_id, session_date, doctor_first_name, doctor_last_name;

    public String getSession_id() {
        return session_id;
    }

    public String getDoctor_id() {
        return doctor_id;
    }

    public String getSession_date() {
        return session_date;
    }

    public String getDoctor_first_name() {
        return doctor_first_name;
    }

    public String getDoctor_last_name() {
        return doctor_last_name;
    }

    public SessionsList() {
    }

    public SessionsList(String session_id, String doctor_id, String session_date, String doctor_first_name, String doctor_last_name) {
        this.session_id = session_id;
        this.doctor_id = doctor_id;
        this.session_date = session_date;
        this.doctor_first_name = doctor_first_name;
        this.doctor_last_name = doctor_last_name;
    }
}
