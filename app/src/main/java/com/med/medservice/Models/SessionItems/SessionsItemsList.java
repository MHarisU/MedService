package com.med.medservice.Models.SessionItems;

import java.io.Serializable;

public class SessionsItemsList implements Serializable {

    public String session_id;
    public String product_id;
    public String product_name;
    public String product_mode;
    public String prescription_comment;
    public String usage;
    public String quantity;
    public String created_at;

    public SessionsItemsList(String session_id, String product_id, String product_name, String product_mode,
                             String prescription_comment, String usage, String quantity, String created_at) {
        this.session_id = session_id;
        this.product_id = product_id;
        this.product_name = product_name;
        this.product_mode = product_mode;
        this.prescription_comment = prescription_comment;
        this.usage = usage;
        this.quantity = quantity;
        this.created_at = created_at;
    }

    public String getSession_id() {
        return session_id;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public String getProduct_mode() {
        return product_mode;
    }

    public String getPrescription_comment() {
        return prescription_comment;
    }

    public String getUsage() {
        return usage;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getCreated_at() {
        return created_at;
    }
}
