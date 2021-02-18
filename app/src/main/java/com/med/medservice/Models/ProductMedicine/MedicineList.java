package com.med.medservice.Models.ProductMedicine;

import java.io.Serializable;

public class MedicineList implements Serializable {

    String medicine_id, medicine_name, medicine_category, medicine_sub_category, medicine_image, medicine_sale_price, medicine_regular_price;
    String medicine_quantity, medicine_short_desc, medicine_description, medicine_stock_status;


    public String getMedicine_id() {
        return medicine_id;
    }

    public String getMedicine_name() {
        return medicine_name;
    }

    public String getMedicine_category() {
        return medicine_category;
    }

    public String getMedicine_sub_category() {
        return medicine_sub_category;
    }

    public String getMedicine_image() {
        return medicine_image;
    }

    public String getMedicine_sale_price() {
        return medicine_sale_price;
    }

    public String getMedicine_regular_price() {
        return medicine_regular_price;
    }

    public String getMedicine_quantity() {
        return medicine_quantity;
    }

    public String getMedicine_short_desc() {
        return medicine_short_desc;
    }

    public String getMedicine_description() {
        return medicine_description;
    }

    public String getMedicine_stock_status() {
        return medicine_stock_status;
    }

    public MedicineList() {
    }

    public MedicineList(String medicine_id, String medicine_name, String medicine_category, String medicine_sub_category, String medicine_image,
                        String medicine_sale_price, String medicine_regular_price, String medicine_quantity, String medicine_short_desc,
                        String medicine_description, String medicine_stock_status) {
        this.medicine_id = medicine_id;
        this.medicine_name = medicine_name;
        this.medicine_category = medicine_category;
        this.medicine_sub_category = medicine_sub_category;
        this.medicine_image = medicine_image;
        this.medicine_sale_price = medicine_sale_price;
        this.medicine_regular_price = medicine_regular_price;
        this.medicine_quantity = medicine_quantity;
        this.medicine_short_desc = medicine_short_desc;
        this.medicine_description = medicine_description;
        this.medicine_stock_status = medicine_stock_status;
    }
}
