package com.med.medservice.Models.PanelLabs;

import java.io.Serializable;

public class PanelLabsList implements Serializable {

    String lab_id, lab_name, lab_price, lab_slug, lab_cpt;

    public void setLab_id(String lab_id) {
        this.lab_id = lab_id;
    }

    public String getLab_id() {
        return lab_id;
    }

    public String getLab_name() {
        return lab_name;
    }

    public String getLab_price() {
        return lab_price;
    }

    public String getLab_slug() {
        return lab_slug;
    }

    public String getLab_cpt() {
        return lab_cpt;
    }

    public PanelLabsList(String lab_name, String lab_price, String lab_slug, String lab_cpt) {
        this.lab_name = lab_name;
        this.lab_price = lab_price;
        this.lab_slug = lab_slug;
        this.lab_cpt = lab_cpt;
    }
}
