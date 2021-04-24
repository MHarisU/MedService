package com.med.medservice.Models.Orders;

public class CartItem {
    public String product_id;
    public String product_qty;
    public String pres_id;
    public String doc_session_id;
    public String product_mode;
    public String item_type;

    public CartItem(String product_id, String product_qty, String pres_id, String doc_session_id, String product_mode, String item_type) {
        this.product_id = product_id;
        this.product_qty = product_qty;
        this.pres_id = pres_id;
        this.doc_session_id = doc_session_id;
        this.product_mode = product_mode;
        this.item_type = item_type;
    }

    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public String getPres_id() {
        return pres_id;
    }

    public String getDoc_session_id() {
        return doc_session_id;
    }

    public String getProduct_mode() {
        return product_mode;
    }

    public String getItem_type() {
        return item_type;
    }
}
