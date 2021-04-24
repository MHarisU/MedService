package com.med.medservice.Models.SessionsPatient;

import java.io.Serializable;

public class SessionsList implements Serializable {

    String session_id, start_time, end_time, provider_notes, created_at, session_status, diagnosis, symptom_id, doctor_first_name, doctor_last_name,
            symptoms_headache, symptoms_fever, symptoms_flu, symptoms_nausea, symptoms_others, symptoms_description;


    public String getSession_id() {
        return session_id;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public String getProvider_notes() {
        return provider_notes;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getSession_status() {
        return session_status;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public String getSymptom_id() {
        return symptom_id;
    }

    public String getDoctor_first_name() {
        return doctor_first_name;
    }

    public String getDoctor_last_name() {
        return doctor_last_name;
    }

    public String getSymptoms_headache() {
        return symptoms_headache;
    }

    public String getSymptoms_fever() {
        return symptoms_fever;
    }

    public String getSymptoms_flu() {
        return symptoms_flu;
    }

    public String getSymptoms_nausea() {
        return symptoms_nausea;
    }

    public String getSymptoms_others() {
        return symptoms_others;
    }

    public String getSymptoms_description() {
        return symptoms_description;
    }

    public SessionsList(String session_id, String start_time, String end_time, String provider_notes, String created_at, String session_status,
                        String diagnosis, String symptom_id, String doctor_first_name, String doctor_last_name, String symptoms_headache, String symptoms_fever,
                        String symptoms_flu, String symptoms_nausea, String symptoms_others, String symptoms_description) {
        this.session_id = session_id;
        this.start_time = start_time;
        this.end_time = end_time;
        this.provider_notes = provider_notes;
        this.created_at = created_at;
        this.session_status = session_status;
        this.diagnosis = diagnosis;
        this.symptom_id = symptom_id;
        this.doctor_first_name = doctor_first_name;
        this.doctor_last_name = doctor_last_name;
        this.symptoms_headache = symptoms_headache;
        this.symptoms_fever = symptoms_fever;
        this.symptoms_flu = symptoms_flu;
        this.symptoms_nausea = symptoms_nausea;
        this.symptoms_others = symptoms_others;
        this.symptoms_description = symptoms_description;
    }
}
