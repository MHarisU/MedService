package com.med.medservice.Models.ProductLabs;

import java.io.Serializable;

public class LabsList implements Serializable {

    String lab_id, lab_panel, lab_name, lab_category, lab_sub_category, lab_image, lab_sale_price, lab_regular_price;
    String lab_quantity, lab_short_desc, lab_description, lab_stock_status;


    public String getLab_id() {
        return lab_id;
    }

    public String getLab_panel() {
        return lab_panel;
    }

    public String getLab_name() {
        return lab_name;
    }

    public String getLab_category() {
        return lab_category;
    }

    public String getLab_sub_category() {
        return lab_sub_category;
    }

    public String getLab_image() {
        return lab_image;
    }

    public String getLab_sale_price() {
        return lab_sale_price;
    }

    public String getLab_regular_price() {
        return lab_regular_price;
    }

    public String getLab_quantity() {
        return lab_quantity;
    }

    public String getLab_short_desc() {
        return lab_short_desc;
    }

    public String getLab_description() {
        return lab_description;
    }

    public String getLab_stock_status() {
        return lab_stock_status;
    }

    public LabsList() {
    }

    public LabsList(String lab_id, String lab_name, String lab_category, String lab_sub_category, String lab_image,
                    String lab_sale_price, String lab_regular_price, String lab_quantity, String lab_short_desc,
                    String lab_description, String lab_stock_status) {
        this.lab_id = lab_id;
        this.lab_name = lab_name;
        this.lab_category = lab_category;
        this.lab_sub_category = lab_sub_category;
        this.lab_image = lab_image;
        if (lab_sale_price != null && !lab_sale_price.equals("null") && !lab_sale_price.equals("")){
            this.lab_sale_price = lab_sale_price;
        }else {
            this.lab_sale_price = "0";
        }
        if (lab_regular_price != null && !lab_regular_price.equals("null") && !lab_regular_price.equals("")){
            this.lab_regular_price = lab_regular_price;
        }else {
            this.lab_regular_price = "0";
        }
        this.lab_quantity = lab_quantity;
        this.lab_short_desc = lab_short_desc;
        this.lab_description = lab_description;
        this.lab_stock_status = lab_stock_status;
    }

    public LabsList(String lab_id, String lab_panel, String lab_name, String lab_category, String lab_sub_category, String lab_image,
                    String lab_sale_price, String lab_regular_price, String lab_quantity, String lab_short_desc,
                    String lab_description, String lab_stock_status) {
        this.lab_id = lab_id;
        this.lab_panel = lab_panel;
        this.lab_name = lab_name;
        this.lab_category = lab_category;
        this.lab_sub_category = lab_sub_category;
        this.lab_image = lab_image;
        if (lab_sale_price != null && !lab_sale_price.equals("null") && !lab_sale_price.equals("")){
            this.lab_sale_price = lab_sale_price;
        }else {
            this.lab_sale_price = "0";
        }
        if (lab_regular_price != null && !lab_regular_price.equals("null") && !lab_regular_price.equals("")){
            this.lab_regular_price = lab_regular_price;
        }else {
            this.lab_regular_price = "0";
        }
        this.lab_quantity = lab_quantity;
        this.lab_short_desc = lab_short_desc;
        this.lab_description = lab_description;
        this.lab_stock_status = lab_stock_status;
    }
}
