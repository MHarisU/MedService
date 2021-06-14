package com.med.medservice.Utils;

public class PrescriptionIDList {
    String prescription_id;
    String item_id;
    String item_type;

    public PrescriptionIDList(String prescription_id, String item_id, String item_type) {
        this.prescription_id = prescription_id;
        this.item_id = item_id;
        this.item_type = item_type;
    }

    public String getPrescription_id() {
        return prescription_id;
    }

    public void setPrescription_id(String prescription_id) {
        this.prescription_id = prescription_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_type() {
        return item_type;
    }

    public void setItem_type(String item_type) {
        this.item_type = item_type;
    }
}
