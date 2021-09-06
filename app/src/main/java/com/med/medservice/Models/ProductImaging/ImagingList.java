package com.med.medservice.Models.ProductImaging;

import java.io.Serializable;

public class ImagingList implements Serializable {
    public int id;
    public String name;
    public String slug;
    public String parent_category;
    public String sub_category;
    public String featured_image;
    public String sale_price;
    public String regular_price;
    public String quantity;
    public String mode;
    public String medicine_type;
    public String is_featured;
    public String short_description;
    public String description;
    public String cpt_code;
    public String test_details;
    public String including_test;
    public String stock_status;


    public ImagingList(int id, String name, String slug, String parent_category, String sub_category, String featured_image, String sale_price, String regular_price, String quantity, String mode, String medicine_type, String is_featured, String short_description, String description, String cpt_code, String test_details, String including_test, String stock_status) {
        this.id = id;
        this.name = name;
        this.slug = slug;
        this.parent_category = parent_category;
        this.sub_category = sub_category;
        this.featured_image = featured_image;
        this.sale_price = sale_price;
        this.regular_price = regular_price;
        this.quantity = quantity;
        this.mode = mode;
        this.medicine_type = medicine_type;
        this.is_featured = is_featured;
        this.short_description = short_description;
        this.description = description;
        this.cpt_code = cpt_code;
        this.test_details = test_details;
        this.including_test = including_test;
        this.stock_status = stock_status;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSlug() {
        return slug;
    }

    public String getParent_category() {
        return parent_category;
    }

    public String getSub_category() {
        return sub_category;
    }

    public String getFeatured_image() {
        return featured_image;
    }

    public String getSale_price() {
        return sale_price;
    }

    public String getRegular_price() {
        return regular_price;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getMode() {
        return mode;
    }

    public String getMedicine_type() {
        return medicine_type;
    }

    public String getIs_featured() {
        return is_featured;
    }

    public String getShort_description() {
        return short_description;
    }

    public String getDescription() {
        return description;
    }

    public String getCpt_code() {
        return cpt_code;
    }

    public String getTest_details() {
        return test_details;
    }

    public String getIncluding_test() {
        return including_test;
    }

    public Object getStock_status() {
        return stock_status;
    }
}
